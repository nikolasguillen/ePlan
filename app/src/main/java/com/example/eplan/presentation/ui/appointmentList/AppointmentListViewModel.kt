package com.example.eplan.presentation.ui.appointmentList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Appointment
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentList.DayChangeAppointment
import com.example.eplan.network.util.isConnectionAvailable
import com.example.eplan.presentation.BaseApplication
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.appointmentList.AppointmentListEvent.DayChangeEvent
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_QUERY = "appointment.state.query.key"

@HiltViewModel
class AppointmentListViewModel
@Inject
constructor(
    getToken: GetToken,
    private val context: BaseApplication,
    private val dayChangeAppointment: DayChangeAppointment,
    private val savedStateHandle: SavedStateHandle
) : EplanViewModel() {

    val appointments = mutableStateListOf<Appointment>()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val date = mutableStateOf(LocalDate.now().toString())
    var isConnectionAvailable by mutableStateOf(false)
        private set

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onTriggerEvent(event: AppointmentListEvent) {
        when (event) {
            is DayChangeEvent -> {
                setDate(event.date)
                dayChange()
            }
        }
    }

    private fun onCreation() {
        savedStateHandle.get<String>(com.example.eplan.presentation.ui.interventionList.STATE_KEY_QUERY)
            ?.let { q ->
                setDate(q)
            }
        dayChange()
    }

    private fun dayChange() {
        isConnectionAvailable = isConnectionAvailable(context = context)

        if (isConnectionAvailable) {
            dayChangeAppointment.execute(token = userToken, query = date.value)
                .onEach { dataState ->
                    _isRefreshing.value = dataState.loading

                    dataState.data?.let { list ->
                        appointments.clear()
                        appointments.addAll(list)
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "dayChange (Appointment): $error")
                        // TODO gestire errori
                    }
                }.launchIn(viewModelScope)
        } else {
            _isRefreshing.value = false
        }
    }

    private fun setDate(date: String) {
        this.date.value = date
        savedStateHandle[STATE_KEY_QUERY] = date
    }
}