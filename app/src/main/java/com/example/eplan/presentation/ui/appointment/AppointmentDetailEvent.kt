package com.example.eplan.presentation.ui.appointment

sealed class AppointmentDetailEvent {

    data class GetAppointmentEvent(
        val id: String
    ) : AppointmentDetailEvent()

    object UpdateAppointmentEvent : AppointmentDetailEvent()

    object DeleteAppointmentEvent : AppointmentDetailEvent()
}
