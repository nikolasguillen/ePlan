package com.cosmobile.eplan.appointment_detail.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.domain.use_cases.DeleteAppointment
import com.cosmobile.eplan.appointment_detail.domain.use_cases.GetAppointmentById
import com.cosmobile.eplan.appointment_detail.domain.use_cases.UpdateAppointment
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent.DeleteAppointmentEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent.GetAppointmentEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent.RefreshAppointmentEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent.UpdateAppointmentEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnActivityNameChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnConfirmUsersList
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDateChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDescriptionChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDismissUsersList
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnEndTimeChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnInterventionChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnMemoChecked
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnPeriodicityChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnPeriodicityEndChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnPlanningChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnStartTimeChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnSubmit
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnUserSelected
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnWarningTimeChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnWarningUnitChanged
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateActivity
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateDescription
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateTime
import com.cosmobile.eplan.core.presentation.BaseApplication
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
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetActivitiesList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_APPOINTMENT = "appointment.state.appointmentId.key"

@HiltViewModel
class AppointmentDetailViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getAppointmentById: GetAppointmentById,
    private val updateAppointment: UpdateAppointment,
    private val getActivitiesList: GetActivitiesList,
    private val deleteAppointment: DeleteAppointment,
    private val validateActivity: ValidateActivity,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle,
    private val networkUtils: NetworkUtils
) : WorkActivityDetailViewModel() {

    var state by mutableStateOf(AppointmentDetailUiState())
        private set

    init {
        getToken(getToken = getToken, onTokenRetrieved = { getActivitiesList() })
    }

    fun onEvent(event: WorkActivityDetailEvent) {
        when (event) {
            is GetAppointmentEvent -> {
                changeQuery(event.id)
                getAppointment(event.date)
            }

            is UpdateAppointmentEvent -> {
                updateAppointment(event.onSuccessfulSubmission)
            }

            is DeleteAppointmentEvent -> {
                deleteAppointment()
            }

            is RefreshAppointmentEvent -> {
                getActivitiesList()
            }
        }
    }

    private fun onCreation() {
        val id = savedStateHandle.get<String?>(NavArguments.APPOINTMENT_ID)
        val date = savedStateHandle.get<String>(NavArguments.DATE)

        if (id == null) {
            createAppointmentManually(LocalDate.parse(date))
        } else {
            onEvent(GetAppointmentEvent(id = id, date = LocalDate.parse(date)))
        }
    }

    private fun createAppointmentManually(date: LocalDate) {
        state = state.copy(
            initialAppointmentState = Appointment(date = date, periodicityEnd = date),
            workActivity = Appointment(date = date, periodicityEnd = date)
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
            }

            is OnToggleActivitySelectorVisibility -> {
                state = state.copy(
                    activitySelectorUiState = state.activitySelectorUiState.copy(
                        isVisible = !state.activitySelectorUiState.isVisible,
                        searchQuery = ""
                    )
                )
            }

            is OnDateChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(date = event.date)
                )
            }

            is OnDescriptionChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(description = event.description)
                )
            }

            is OnEndTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(end = event.time)
                )
            }

            is OnInterventionChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(accounted = event.selected)
                )
            }

            is OnUserSelected -> {
                state = state.copy(
                    usersSelectorUiState = state.usersSelectorUiState?.copy(
                        invitedPeople = state.usersSelectorUiState?.invitedPeople?.map {
                            if (it.user?.id == event.user.id) {
                                it.copy(isSelected = !it.isSelected)
                            } else {
                                it
                            }
                        } ?: emptyList()
                    )
                )
            }

            is OnConfirmUsersList -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(
                        invited = state.usersSelectorUiState?.invitedPeople
                            ?.filter { it.isSelected }
                            ?.mapNotNull { it.user } ?: emptyList()
                    ),
                    usersSelectorUiState = null
                )
            }

            is OnDismissUsersList -> {
                state = state.copy(usersSelectorUiState = null)
            }

            is OnMemoChecked -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(memo = state.workActivity?.memo?.not() == true)
                )
            }

            is OnPeriodicityChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(periodicity = event.periodicity)
                )
            }

            is OnPeriodicityEndChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(periodicityEnd = event.date)
                )
            }

            is OnPlanningChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(planning = event.selected)
                )
            }

            is OnStartTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(start = event.time)
                )
            }

            is OnWarningTimeChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(warningTime = event.warningTime)
                )
            }

            is OnWarningUnitChanged -> {
                state = state.copy(
                    workActivity = state.workActivity?.copy(warningUnit = event.warningUnit)
                )
            }

            is OnSubmit -> {
                submitData(event.onSuccessfullSubmission)
            }
        }
    }

    override fun checkChanges(): Boolean {
        return state.hasChanged()
    }

    private fun submitData(onSuccessfulSubmission: (() -> Unit)? = null) {
        state.workActivity?.let { appointment ->
            // TODO implementare controllo su data impostata nella periodicità > data appuntamento
            val activityResult = validateActivity.execute(appointment.activityName)
            val descriptionResult = validateDescription.execute(appointment.description)
            val timeResult = validateTime.execute(appointment.start, appointment.end)

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
            updateAppointment(onSuccessfulSubmission)
        }
    }

    private fun getAppointment(date: LocalDate) {
        if (networkUtils.isConnectionAvailable()) {
            getAppointmentById.execute(
                token = userToken,
                id = query,
                date = date
            ).onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { newAppointment ->
                    val activity =
                        state.activitySelectorUiState.activities.firstOrNull { it.id == newAppointment.activityId }

                    withContext(Dispatchers.Main) {
                        state = state.copy(
                            initialAppointmentState = newAppointment,
                            workActivity = newAppointment,
                            enableDeletion = true
                        )
                    }

                    if (activity != null) {
                        state = state.copy(
                            initialAppointmentState = state.initialAppointmentState?.copy(
                                activityName = activity.name
                            ),
                            workActivity = newAppointment.copy(activityName = activity.name)
                        )
                    }
                }

                dataState.error?.let { error ->
                    Log.e(GENERIC_DEBUG_TAG, "getAppointment: $error")
                    validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
                }
            }.launchIn(viewModelScope)
        } else {
            state = state.copy(showAbsentConnectionScreen = true)
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.NoConnection) }
        }
    }

    private fun updateAppointment(onSuccessfulSubmission: (() -> Unit)? = null) {
        if (networkUtils.isConnectionAvailable()) {
            state.workActivity?.let {
                updateAppointment.execute(token = userToken, appointment = it)
                    .onEach { dataState ->
                        sending = dataState.loading

                        dataState.data?.let { result ->
                            if (result) {
                                validationEventChannel.send(ValidationEvent.UpdateSuccess)
                                onSuccessfulSubmission?.invoke()
                            }
                        }

                        dataState.error?.let { error ->
                            Log.e(GENERIC_DEBUG_TAG, "updateAppointment: $error")
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

    private fun deleteAppointment() {
        state.workActivity?.let {
            savedStateHandle.remove<String>(STATE_KEY_APPOINTMENT)
            if (networkUtils.isConnectionAvailable()) {
                deleteAppointment.execute(token = userToken, id = it.id)
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
                Log.e(GENERIC_DEBUG_TAG, "getActivitiesList (Appointment detail): $error")
                validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
            }
        }.launchIn(viewModelScope)
    }

    override fun changeQuery(query: String) {
        this.query = query
        savedStateHandle[STATE_KEY_APPOINTMENT] = query
    }
}