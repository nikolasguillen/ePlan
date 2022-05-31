package com.example.eplan.presentation.ui.vacationRequest

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.vacationRequest.RequestVacation
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestEvent.PeriodRequestEvent
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestEvent.SingleDayRequestEvent
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VacationRequestViewModel
@Inject
constructor(
    getToken: GetToken,
    private val requestVacation: RequestVacation
) : EplanViewModel() {

    var singleDayVacancy by mutableStateOf(true)
    var successfulVacationRequest: MutableState<Boolean?> = mutableStateOf(null)
    val singleDate = mutableStateOf(LocalDate.now())
    val startDate = mutableStateOf(LocalDate.now())
    val endDate = mutableStateOf(LocalDate.now())

    init {
        getToken(getToken = getToken)
    }

    fun onTriggerEvent(event: VacationRequestEvent) {
        when (event) {
            is SingleDayRequestEvent -> {
                singleDayRequest()
            }
            is PeriodRequestEvent -> {
                periodRequest()
            }
        }
    }

    private fun singleDayRequest() {
        requestVacation.execute(token = userToken, startDate = startDate.value)
            .onEach { dataState ->
                sending = dataState.loading

                dataState.data?.let { success ->
                    successfulVacationRequest.value = success
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "singleDayRequest (vacation request): $error")
                    successfulVacationRequest.value = false
                }
            }.launchIn(viewModelScope)
    }

    private fun periodRequest() {
        requestVacation.execute(
            token = userToken,
            startDate = startDate.value,
            endDate = endDate.value
        ).onEach { dataState ->
            sending = dataState.loading

            dataState.data?.let { success ->
                successfulVacationRequest.value = success
            }

            dataState.error?.let { error ->
                Log.e(TAG, "periodRequest (vacation request): $error")
                successfulVacationRequest.value = false
            }
        }.launchIn(viewModelScope)
    }
}