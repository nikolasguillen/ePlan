package com.cosmobile.eplan.vacation_request.ui

sealed class VacationRequestEvent {
    data class RequestEvent(val startDate: Long?, val endDate: Long?) : VacationRequestEvent()
}
