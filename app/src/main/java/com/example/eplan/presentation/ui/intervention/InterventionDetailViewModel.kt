package com.example.eplan.presentation.ui.intervention

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Activity
import com.example.eplan.domain.model.Intervention
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.interventionDetail.GetInterventionById
import com.example.eplan.interactors.interventionDetail.UpdateIntervention
import com.example.eplan.interactors.workActivityDetail.GetActivitiesList
import com.example.eplan.interactors.workActivityDetail.ValidateActivity
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.network.util.isConnectionAvailable
import com.example.eplan.presentation.BaseApplication
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.DeleteInterventionEvent
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.GetInterventionEvent
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.RefreshInterventionEvent
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.UpdateInterventionEvent
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.ActivityIdChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.ActivityNameChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.DateChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.DescriptionChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.EndChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.KmChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.MovingTimeChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.StartChanged
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.Submit
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

const val STATE_KEY_INTERVENTION_ID = "intervention.state.interventionId.key"

@HiltViewModel
class InterventionDetailViewModel
@Inject
constructor(
    getToken: GetToken,
    private val context: BaseApplication,
    private val getInterventionById: GetInterventionById,
    private val updateIntervention: UpdateIntervention,
    private val getActivitiesList: GetActivitiesList,
    private val validateActivity: ValidateActivity,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle
) : WorkActivityDetailViewModel() {

    private var initialState: MutableState<Intervention?> = mutableStateOf(null)
    var intervention: MutableState<Intervention?> = mutableStateOf(null)
        private set

    var activitiesList = mutableStateListOf<Activity?>(null)
        private set

    var activitySearchQuery by mutableStateOf("")

    init {
        getToken(getToken = getToken, onTokenRetrieved = { getActivitiesList() })
    }

    fun onTriggerEvent(event: InterventionDetailEvent) {
        when (event) {
            is GetInterventionEvent -> {
                changeQuery(event.id)
                getIntervention()
            }

            is UpdateInterventionEvent -> {
                updateIntervention()
            }

            is DeleteInterventionEvent -> {
                deleteIntervention()
            }

            is RefreshInterventionEvent -> {
                getActivitiesList()
            }
        }
    }

    private fun onCreation() {
        val id = savedStateHandle.get<String?>("activityId")
        val date = savedStateHandle.get<String>("date")
        val start = savedStateHandle.get<String?>("start")
        val end = savedStateHandle.get<String?>("end")

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
            onTriggerEvent(GetInterventionEvent(id = id))
        }
    }

    private fun createInterventionManually(date: LocalDate) {
        intervention.value = Intervention(date = date)
        initialState.value = Intervention(date = date)
    }

    private fun createRecordedIntervention(date: LocalDate, start: LocalTime, end: LocalTime) {
        intervention.value = Intervention(date = date, start = start, end = end)
        initialState.value = Intervention(date = date, start = start, end = end)
    }

    fun onFormEvent(event: InterventionFormEvent) {
        when (event) {
            is ActivityNameChanged -> {
                intervention.value = intervention.value?.copy(activityName = event.name)
            }

            is ActivityIdChanged -> {
                intervention.value = intervention.value?.copy(
                    activityId = event.id,
                    activityName = activitiesList.first { it!!.id == event.id }!!.name
                )
            }

            is DescriptionChanged -> {
                intervention.value = intervention.value?.copy(description = event.description)
            }

            is DateChanged -> {
                intervention.value =
                    intervention.value?.copy(date = event.date)
            }

            is StartChanged -> {
                intervention.value = intervention.value?.copy(start = event.time)
            }

            is EndChanged -> {
                intervention.value = intervention.value?.copy(end = event.time)
            }

            is MovingTimeChanged -> {
                intervention.value = intervention.value?.copy(movingTime = event.time)
            }

            is KmChanged -> {
                intervention.value = intervention.value?.copy(km = event.km)
            }

            is Submit -> {
                submitData()
            }
        }
    }

    override fun checkChanges(): Boolean {
        return intervention.value != initialState.value
    }

    private fun submitData() {
        intervention.value?.let { intervention ->
            // TODO quando arrivo qua devo aver già popolato la mappa di attività (id, nome)
            val activityResult = validateActivity.execute(
                intervention.activityName,
                activitiesList.map { it?.name ?: "" })
            val descriptionResult = validateDescription.execute(intervention.description)
            val timeResult = validateTime.execute(intervention.start, intervention.end)

            val hasErrors = listOf(
                activityResult,
                descriptionResult,
                timeResult
            ).any { result -> !result.successful }

            if (hasErrors) {
                this.intervention.value = this.intervention.value?.copy(
                    activityIdError = activityResult.errorMessage,
                    descriptionError = descriptionResult.errorMessage,
                    timeError = timeResult.errorMessage
                )
                return
            }
            onTriggerEvent(UpdateInterventionEvent)
        }
    }

    private fun getIntervention() {
        if (isConnectionAvailable(context = context)) {
            getInterventionById.execute(token = userToken, id = query).onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { newIntervention ->
                    val activity =
                        activitiesList.firstOrNull { it?.id == newIntervention.activityId }
                    initialState.value = newIntervention
                    intervention.value = newIntervention
                    if (activity != null) {
                        initialState.value = initialState.value?.copy(activityName = activity.name)
                        intervention.value = intervention.value?.copy(activityName = activity.name)
                    }
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getIntervention: $error")
                    validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateIntervention() {
        if (isConnectionAvailable(context = context)) {
            intervention.value?.let {
                updateIntervention.execute(token = userToken, intervention = it)
                    .onEach { dataState ->
                        sending = dataState.loading

                        dataState.data?.let { result ->
                            if (result) {
                                validationEventChannel.send(ValidationEvent.UpdateSuccess)
                            }
                        }

                        dataState.error?.let { error ->
                            Log.e(TAG, "updateIntervention: $error")
                            validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                        }
                    }.launchIn(viewModelScope)
            }
        } else {
            sending = false
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
        }
    }

    private fun deleteIntervention() {
        intervention.value?.let {
            //TODO
        }
        savedStateHandle.remove<String>(STATE_KEY_INTERVENTION_ID)
    }

    private fun getActivitiesList() {
        if (isConnectionAvailable(context = context)) {
            getActivitiesList.execute(token = userToken).onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { result ->
                    this.activitiesList.clear()
                    this.activitiesList.addAll(result)
                    onCreation()
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getActivitiesList (Intervention Detail): $error")
                    validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun changeQuery(query: String) {
        this.query = query
        savedStateHandle[STATE_KEY_INTERVENTION_ID] = query
    }
}