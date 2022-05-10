package com.example.eplan.presentation.ui.appointment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.User
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentDetail.GetAppointmentById
import com.example.eplan.interactors.appointmentDetail.UpdateAppointment
import com.example.eplan.interactors.workActivityDetail.ValidateActivity
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailEvent.*
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.fromDateToLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val validateActivity: ValidateActivity,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle
) : WorkActivityDetailViewModel() {

    private var initialState: MutableState<Appointment?> = mutableStateOf(null)
    var appointment: MutableState<Appointment?> = mutableStateOf(null)
        private set

    /* Variabili necessarie per non dover direttamente modificare l'appuntamento
    * mentre aggiungo/rimuovo invitati dalla form */
    private val invitedListBackup = mutableStateListOf<User>()
    private val tempInvitedList = mutableStateListOf<User>()

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onTriggerEvent(event: AppointmentDetailEvent) {
        when (event) {
            is GetAppointmentEvent -> {
                changeQuery(event.id)
                getAppointment()
            }
            is UpdateAppointmentEvent -> {
                updateAppointment()
            }
            is DeleteAppointmentEvent -> {
                deleteAppointment()
            }
        }
    }

    private fun onCreation() {
        val id = savedStateHandle.get<String?>("appointmentId")
        val date = savedStateHandle.get<String>("date")

        if (id == null) {
            createAppointmentManually(LocalDate.parse(date))
        } else {
            onTriggerEvent(GetAppointmentEvent(id))
        }
    }

    private fun createAppointmentManually(date: LocalDate) {
        initialState.value = Appointment(date = date, periodicityEnd = date)
        appointment.value = Appointment(date = date, periodicityEnd = date)
        appointment.value?.invited?.let {
            invitedListBackup.addAll(it)
            tempInvitedList.addAll(it)
        }
    }

    fun isUserInvited(user: User): Boolean {
        return tempInvitedList.contains(user)
    }

    fun onFormEvent(event: AppointmentFormEvent) {
        when (event) {
            is ActivityNameChanged -> {
                appointment.value = appointment.value?.copy(activityName = event.name)
            }
            is DateChanged -> {
                appointment.value = appointment.value?.copy(date = fromDateToLocalDate(event.date))
            }
            is DescriptionChanged -> {
                appointment.value = appointment.value?.copy(description = event.description)
            }
            is EndChanged -> {
                appointment.value = appointment.value?.copy(end = event.time)
            }
            is InterventionChanged -> {
                appointment.value = appointment.value?.copy(intervention = event.selected)
            }
            is AddInvited -> {
                tempInvitedList.add(event.invited)
//                }
            }
            is RemoveInvited -> {
                tempInvitedList.remove(event.invited)
            }
            is ConfirmInvitedList -> {
                val confirmedList = mutableListOf<User>()
                confirmedList.addAll(tempInvitedList)
                appointment.value = appointment.value?.copy(invited = confirmedList)
                invitedListBackup.clear()
                invitedListBackup.addAll(tempInvitedList)
            }
            is DismissInvitedList -> {
                val restoredList = mutableListOf<User>()
                restoredList.addAll(invitedListBackup)
                appointment.value = appointment.value?.copy(invited = restoredList)
                tempInvitedList.clear()
                tempInvitedList.addAll(invitedListBackup)
            }
            is MemoChanged -> {
                appointment.value = appointment.value?.copy(memo = event.selected)
            }
            is PeriodicityChanged -> {
                appointment.value = appointment.value?.copy(periodicity = event.periodicity)
            }
            is PeriodicityEndChanged -> {
                appointment.value = appointment.value?.copy(periodicityEnd = fromDateToLocalDate(event.date))
            }
            is PlanningChanged -> {
                appointment.value = appointment.value?.copy(planning = event.selected)
            }
            is StartChanged -> {
                appointment.value = appointment.value?.copy(start = event.time)
            }
            is WarningTimeChanged -> {
                appointment.value = appointment.value?.copy(warningTime = event.warningTime)
            }
            is WarningUnitChanged -> {
                appointment.value = appointment.value?.copy(warningUnit = event.warningUnit)
            }
            Submit -> {
                submitData()
            }
        }
    }

    override fun checkChanges(): Boolean {
        return appointment.value != initialState.value
    }

    private fun submitData() {
        appointment.value?.let {
            // TODO quando arrivo qua devo aver già popolato la mappa di attività (id, nome)
            // TODO implementare controllo su data impostata nella periodicità > data appuntamento
            val activityResult = validateActivity.execute(it.activityName, mapOf())
            val descriptionResult = validateDescription.execute(it.description)
            val timeResult = validateTime.execute(it.start, it.end)

            val hasErrors = listOf(
                activityResult,
                descriptionResult,
                timeResult
            ).any { result -> !result.successful }

            if (hasErrors) {
                appointment.value = appointment.value?.copy(
                    activityIdError = activityResult.errorMessage,
                    descriptionError = descriptionResult.errorMessage,
                    timeError = timeResult.errorMessage
                )
                return
            }
            onTriggerEvent(UpdateAppointmentEvent)
        }
    }

    private fun getAppointment() {
        getAppointmentById.execute(token = userToken, id = query).onEach { dataState ->
            retrieving = dataState.loading

            dataState.data?.let { newAppointment ->
                initialState.value = newAppointment
                appointment.value = newAppointment
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getAppointment: $error")
                validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
            }
        }.launchIn(viewModelScope)
    }

    private fun updateAppointment() {
        appointment.value?.let {
            updateAppointment.execute(token = userToken, appointment = it)
                .onEach { dataState ->
                    sending = dataState.loading

                    dataState.data?.let { result ->
                        if (result) {
                            validationEventChannel.send(ValidationEvent.UpdateSuccess)
                        }
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "updateAppointment: $error")
                        validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun deleteAppointment() {
        appointment.value?.let {
            //TODO
        }
        savedStateHandle.remove<String>(STATE_KEY_APPOINTMENT)
    }

    override fun changeQuery(query: String) {
        this.query = query
        savedStateHandle.set(STATE_KEY_APPOINTMENT, query)
    }
}