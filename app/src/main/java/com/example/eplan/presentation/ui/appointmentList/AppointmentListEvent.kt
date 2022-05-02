package com.example.eplan.presentation.ui.appointmentList

sealed class AppointmentListEvent {

    data class DayChangeEvent(
        val date: String
    ): AppointmentListEvent()
}