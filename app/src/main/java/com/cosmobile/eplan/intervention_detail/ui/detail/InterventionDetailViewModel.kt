package com.cosmobile.eplan.intervention_detail.ui.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateActivity
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateDescription
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateTime
import com.cosmobile.eplan.core.presentation.ValidationEvent
import com.cosmobile.eplan.core.presentation.WorkActivityDetailViewModel
import com.cosmobile.eplan.core.presentation.navigation.NavArguments
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnActivityQueryChanged
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnActivitySelected
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnToggleActivitySelectorVisibility
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.NetworkUtils
import com.cosmobile.eplan.core.util.UiText
import com.cosmobile.eplan.intervention_detail.domain.use_cases.DeleteIntervention
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetActivitiesList
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetDescriptionSuggestions
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetInterventionById
import com.cosmobile.eplan.intervention_detail.domain.use_cases.UpdateIntervention
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent.DeleteInterventionEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent.GetInterventionEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent.RefreshInterventionEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent.UpdateInterventionEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnActivityNameChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnDateChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnDescriptionUpdated
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnEndTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnKmChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnMovingTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnStartTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnSubmit
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnToggleSuggestionsVisibility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

const val STATE_KEY_INTERVENTION_ID = "intervention.state.interventionId.key"

@HiltViewModel
class InterventionDetailViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getInterventionById: GetInterventionById,
    private val updateIntervention: UpdateIntervention,
    private val getActivitiesList: GetActivitiesList,
    private val deleteIntervention: DeleteIntervention,
    private val getDescriptionSuggestions: GetDescriptionSuggestions,
    private val validateActivity: ValidateActivity,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle,
    private val networkUtils: NetworkUtils
) : WorkActivityDetailViewModel() {

    var state by mutableStateOf(InterventionDetailUiState())
        private set

    init {
        getToken(getToken = getToken, onTokenRetrieved = { getActivitiesList() })
    }

    fun onEvent(event: WorkActivityDetailEvent) {
        when (event) {
            is GetInterventionEvent -> {
                changeQuery(event.id)
                getIntervention()
            }

            is UpdateInterventionEvent -> updateIntervention()

            is DeleteInterventionEvent -> deleteIntervention()

            is RefreshInterventionEvent -> getActivitiesList()
        }
    }

    private fun onCreation() {
        val id = savedStateHandle.get<String?>(NavArguments.INTERVENTION_ID)
        val date = savedStateHandle.get<String>(NavArguments.DATE)
        val start = savedStateHandle.get<String?>(NavArguments.START)
        val end = savedStateHandle.get<String?>(NavArguments.END)

        if (id == null) {
            if (start == null && end == null) {
                createInterventionManually(date = LocalDate.parse(date))
            } else {
                createRecordedIntervention(
                    date = LocalDate.parse(date),
                    start = LocalTime.parse(start),
                    end = LocalTime.parse(end)
                )
            }
        } else {
            onEvent(GetInterventionEvent(id = id))
        }
    }

    private fun createInterventionManually(date: LocalDate) {
        state = state.copy(
            initialInterventionState = Intervention(date = date),
            workActivity = Intervention(date = date)
        )
    }

    private fun createRecordedIntervention(date: LocalDate, start: LocalTime, end: LocalTime) {
        state = state.copy(
            initialInterventionState = Intervention(date = date, start = start, end = end),
            workActivity = Intervention(date = date, start = start, end = end)
        )
    }

    fun onFormEvent(event: WorkActivityFormEvent) {
        when (event) {
            is OnActivityNameChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(activityName = event.name)
                )
            }

            is OnActivityQueryChanged -> {
                state = state.copy(
                    activitySelectorUiState = state.activitySelectorUiState.copy(searchQuery = event.query)
                )
            }

            is OnActivitySelected -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(
                        activityId = event.activity.id,
                        activityName = event.activity.name
                    ),
                    activitySelectorUiState = state.activitySelectorUiState.copy(isVisible = false)
                )
                state.workActivity?.date?.let { date ->
                    getDescriptionSuggestions(
                        token = userToken,
                        date = date,
                        activityId = event.activity.id
                    )
                }
            }

            is OnToggleActivitySelectorVisibility -> {
                state = state.copy(
                    activitySelectorUiState = state.activitySelectorUiState.copy(
                        isVisible = !state.activitySelectorUiState.isVisible,
                        searchQuery = ""
                    )
                )
            }

            is OnDescriptionUpdated -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(description = event.description)
                )
            }

            is OnToggleSuggestionsVisibility -> {
                state = state.copy(
                    showDescriptionSuggestions = !state.showDescriptionSuggestions
                )
            }

            is OnDateChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(date = event.date)
                )
            }

            is OnStartTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(start = event.time)
                )
            }

            is OnEndTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(end = event.time)
                )
            }

            is OnMovingTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(movingTime = event.time)
                )
            }

            is OnKmChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(km = event.km)
                )
            }

            is OnSubmit -> {
                submitData(event.onSuccessfulSubmission)
            }
        }
    }

    override fun checkChanges(): Boolean = state.hasChanged()

    private fun submitData(onSuccessfulSubmission: (() -> Unit)? = null) {
        state.workActivity?.let { intervention ->
            val activityResult = validateActivity.execute(intervention.activityName)
            val descriptionResult = validateDescription.execute(intervention.description)
            val timeResult = validateTime.execute(intervention.start, intervention.end)

            val hasErrors = listOf(
                activityResult,
                descriptionResult,
                timeResult
            ).any { result -> !result.successful }

            if (hasErrors) {
                state = state.copy(
                    workActivity = state.workActivity?.copy(
                        activityIdError = activityResult.errorMessage,
                        descriptionError = descriptionResult.errorMessage,
                        timeError = timeResult.errorMessage

                    )
                )
                return
            }
            updateIntervention(onSuccessfulSubmission)
        }
    }

    private fun getIntervention() {
        if (networkUtils.isConnectionAvailable()) {
            getInterventionById.execute(token = userToken, id = query).onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { newIntervention ->
                    val activity =
                        state.activitySelectorUiState.activities.firstOrNull { it.id == newIntervention.activityId }

                    withContext(Dispatchers.Main) {
                        state = state.copy(
                            initialInterventionState = newIntervention,
                            workActivity = newIntervention,
                            enableDeletion = true
                        )
                    }
                    if (activity != null) {
                        withContext(Dispatchers.Main) {
                            state = state.copy(
                                initialInterventionState = state.initialInterventionState?.copy(
                                    activityName = activity.name
                                ),
                                workActivity = state.workActivity?.copy(activityName = activity.name)
                            )
                        }
                    }
                }

                dataState.error?.let { error ->
                    Log.e(GENERIC_DEBUG_TAG, "getIntervention: $error")
                    validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                }
            }.launchIn(viewModelScope)
        } else {
            state = state.copy(showAbsentConnectionScreen = true)
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
        }
    }

    private fun updateIntervention(onSuccessfulSubmission: (() -> Unit)? = null) {
        if (networkUtils.isConnectionAvailable()) {
            state.workActivity?.let {
                updateIntervention.execute(token = userToken, intervention = it)
                    .onEach { dataState ->
                        sending = dataState.loading

                        dataState.data?.let { result ->
                            if (result) {
                                validationEventChannel.send(ValidationEvent.UpdateSuccess)
                                onSuccessfulSubmission?.invoke()
                            }
                        }

                        dataState.error?.let { error ->
                            Log.e(GENERIC_DEBUG_TAG, "updateIntervention: $error")
                            validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                        }
                    }.launchIn(viewModelScope)
            }
        } else {
            sending = false
            state = state.copy(showAbsentConnectionScreen = true)
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
        }
    }

    private fun deleteIntervention() {
        state.workActivity?.let {
            savedStateHandle.remove<String>(STATE_KEY_INTERVENTION_ID)
            if (networkUtils.isConnectionAvailable()) {
                deleteIntervention.execute(token = userToken, id = it.id)
                    .onEach { dataState ->
                        dataState.data?.let { result ->
                            if (result) {
                                validationEventChannel.send(ValidationEvent.DeleteSuccess)
                            } else {
                                validationEventChannel.send(
                                    ValidationEvent.SubmitError(
                                        error = UiText.StringResource(
                                            R.string.errore_sconosciuto
                                        )
                                    )
                                )
                            }
                        }

                        dataState.error?.let { error ->
                            Log.e(GENERIC_DEBUG_TAG, "deleteIntervention: $error")
                            validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                        }
                    }.launchIn(viewModelScope)
            } else {
                sending = false
                state = state.copy(showAbsentConnectionScreen = true)
                viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
            }
        }
    }

    private fun getActivitiesList() {
        if (networkUtils.isConnectionAvailable()) {
            getActivitiesList.execute(
                token = userToken,
                date = state.workActivity?.date ?: LocalDate.now()
            ).onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { result ->
                    state = state.copy(
                        activitySelectorUiState = state.activitySelectorUiState.copy(activities = result)
                    )
                    onCreation()
                }

                dataState.error?.let { error ->
                    Log.e(GENERIC_DEBUG_TAG, "getActivitiesList (Intervention Detail): $error")
                    validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getDescriptionSuggestions(token: String, date: LocalDate, activityId: String) {
        if (networkUtils.isConnectionAvailable()) {
            getDescriptionSuggestions.invoke(token = token, date = date, activityId = activityId)
                .onEach { dataState ->
                    dataState.data?.let { result ->
                        state = state.copy(
                            descriptionSuggestions = result,
                            showDescriptionSuggestions = result.isNotEmpty()
                        )
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "getDescriptionSuggestions: $error")
                        validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                    }
                }.launchIn(viewModelScope)
        } else {
            state = state.copy(showAbsentConnectionScreen = true)
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
        }
    }

    override fun changeQuery(query: String) {
        this.query = query
        savedStateHandle[STATE_KEY_INTERVENTION_ID] = query
    }

    companion object {
        private const val TAG = "InterventionDetailViewModel"
    }
}