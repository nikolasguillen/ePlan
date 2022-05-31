package com.example.eplan.presentation.ui.vacationRequest

sealed class VacationRequestEvent {
    object SingleDayRequestEvent : VacationRequestEvent()
    object PeriodRequestEvent : VacationRequestEvent()
}
