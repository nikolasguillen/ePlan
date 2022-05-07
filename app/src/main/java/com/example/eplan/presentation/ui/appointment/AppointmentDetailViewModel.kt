package com.example.eplan.presentation.ui.appointment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.util.Periodicity
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentDetail.GetAppointmentById
import com.example.eplan.interactors.appointmentDetail.UpdateAppointment
import com.example.eplan.interactors.interventionDetail.ValidateTime
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailEvent.*
import com.example.eplan.presentation.ui.intervention.STATE_KEY_INTERVENTION_ID
import com.example.eplan.presentation.util.TAG
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
    private val getToken: GetToken,
    private val getAppointmentById: GetAppointmentById,
    private val submitAppointment: UpdateAppointment,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle
) : WorkActivityDetailViewModel() {

    private var initialState: MutableState<Appointment?> = mutableStateOf(null)
    var appointment: MutableState<Appointment?> = mutableStateOf(null)
        private set

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
            initialState.value = Appointment(date = LocalDate.parse(date))
            appointment.value = Appointment(date = LocalDate.parse(date))
        }
    }

    override fun checkChanges(): Boolean {
        return appointment.value != initialState.value
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
            submitAppointment.execute(token = userToken, appointment = it)
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