package com.example.eplan.presentation.ui.vacationRequest

sealed class VacationRequestEvent {
    data class RequestEvent(val startDate: Long?, val endDate: Long?) : VacationRequestEvent()
}
