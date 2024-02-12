package com.example.eplan.presentation.ui.vacationRequest

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.vacationRequest.RequestVacation
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestEvent.RequestEvent
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class VacationRequestViewModel
@Inject
constructor(
    getToken: GetToken,
    private val requestVacation: RequestVacation
) : EplanViewModel() {
    var successfulVacationRequest: MutableState<Boolean?> = mutableStateOf(null)

    private val validationEventChannel = Channel<String>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        getToken(getToken = getToken)
    }

    fun onTriggerEvent(event: VacationRequestEvent) {
        when (event) {
            is RequestEvent -> {
                requestVacancy(event.startDate, event.endDate)
            }
        }
    }

    private fun requestVacancy(startDate: Long?, endDate: Long?) {
        requestVacation.execute(token = userToken, startDateMillis = startDate, endDateMillis = endDate)
            .onEach { dataState ->
                sending = dataState.loading

                dataState.data?.let { success ->
                    successfulVacationRequest.value = success
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "singleDayRequest (vacation request): $error")
                    successfulVacationRequest.value = false
                    validationEventChannel.send(error)
                }
            }.launchIn(viewModelScope)
    }
}