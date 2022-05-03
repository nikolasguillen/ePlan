package com.example.eplan.presentation.ui.appointmentList

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Appointment
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentList.DayChangeAppointment
import com.example.eplan.presentation.ui.appointmentList.AppointmentListEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_QUERY = "appointment.state.query.key"

@HiltViewModel
class AppointmentListViewModel
@Inject
constructor(
    private val dayChangeAppointment: DayChangeAppointment,
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var userToken = USER_TOKEN
    val appointments = mutableStateListOf<Appointment>()
    val loading = mutableStateOf(false)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val date = mutableStateOf(LocalDate.now().toString())

    init {
        getToken()
    }

    fun onTriggerEvent(event: AppointmentListEvent) {
        when(event) {
            is DayChangeEvent -> {
                setDate(event.date)
                dayChange()
            }
        }
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->
            loading.value = dataState.loading
            _isRefreshing.value = dataState.loading

            dataState.data?.let { token ->
                userToken += token
                savedStateHandle.get<String>(com.example.eplan.presentation.ui.interventionList.STATE_KEY_QUERY)?.let { q ->
                    setDate(q)
                }
                dayChange()
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun dayChange() {
        dayChangeAppointment.execute(token = userToken, query = date.value).onEach { dataState ->
            loading.value = dataState.loading
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
    }

    private fun setDate(date: String) {
        this.date.value = date
        savedStateHandle.set(STATE_KEY_QUERY, date)
    }
}