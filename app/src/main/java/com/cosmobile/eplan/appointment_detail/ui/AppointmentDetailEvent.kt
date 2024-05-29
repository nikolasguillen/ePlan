package com.cosmobile.eplan.appointment_detail.ui

import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailEvent
import java.time.LocalDate

sealed class AppointmentDetailEvent : WorkActivityDetailEvent {
    data class GetAppointmentEvent(
        val id: String,
        val date: LocalDate
    ) : AppointmentDetailEvent()

    data class UpdateAppointmentEvent(val onSuccessfulSubmission: () -> Unit) : AppointmentDetailEvent()
    data object DeleteAppointmentEvent : AppointmentDetailEvent()
    data object RefreshAppointmentEvent : AppointmentDetailEvent()
}
