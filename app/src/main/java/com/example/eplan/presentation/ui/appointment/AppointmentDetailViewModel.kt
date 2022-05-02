package com.example.eplan.presentation.ui.appointment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Appointment
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentDetail.GetAppointmentById
import com.example.eplan.interactors.appointmentDetail.UpdateAppointment
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.presentation.ui.appointment.AppointmentDetailEvent.*
import com.example.eplan.presentation.ui.workActivity.STATE_KEY_ACTIVITY
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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
) : ViewModel() {

    private var userToken = USER_TOKEN
    private var query = ""
    var retrieving by mutableStateOf(false)
        private set
    var sending by mutableStateOf(false)
        private set
    private var initialState: MutableState<Appointment?> = mutableStateOf(null)
    var appointment: MutableState<Appointment?> = mutableStateOf(null)
        private set
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        getToken()
    }

    fun onTriggerEvent(event: AppointmentDetailEvent) {
        when (event) {
            is GetAppointmentEvent -> {
                setQuery(event.id)
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

    private fun getToken() {
        getToken.execute().onEach { dataState ->
            retrieving = dataState.loading

            dataState.data?.let { token ->
                userToken += token
                savedStateHandle.get<String>(STATE_KEY_ACTIVITY)?.let { appointmentId ->
                    onTriggerEvent(GetAppointmentEvent(appointmentId))
                }
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
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

    private fun setQuery(query: String) {
        this.query = query
        savedStateHandle.set(STATE_KEY_APPOINTMENT, query)
    }

    sealed class ValidationEvent {
        object UpdateSuccess : ValidationEvent()
        data class SubmitError(val error: String) : ValidationEvent()
        data class RetrieveError(val error: String) : ValidationEvent()
    }
}