package com.cosmobile.eplan.appointment_detail.ui

import com.cosmobile.eplan.appointment_detail.domain.model.Periodicity
import com.cosmobile.eplan.appointment_detail.domain.model.User
import com.cosmobile.eplan.appointment_detail.domain.model.WarningUnit
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import java.time.LocalDate
import java.time.LocalTime

sealed class AppointmentFormEvent : WorkActivityFormEvent {
    data class OnActivityNameChanged(val name: String) : AppointmentFormEvent()
    data class OnDescriptionChanged(val description: String) : AppointmentFormEvent()
    data class OnDateChanged(val date: LocalDate) : AppointmentFormEvent()
    data class OnStartTimeChanged(val time: LocalTime) : AppointmentFormEvent()
    data class OnEndTimeChanged(val time: LocalTime) : AppointmentFormEvent()
    data class OnPlanningChanged(val selected: Boolean) : AppointmentFormEvent()
    data class OnInterventionChanged(val selected: Boolean) : AppointmentFormEvent()
    data class OnUserSelected(val user: User) : AppointmentFormEvent()
    data object OnConfirmUsersList : AppointmentFormEvent()
    data object OnDismissUsersList : AppointmentFormEvent()
    data class OnPeriodicityChanged(val periodicity: Periodicity) : AppointmentFormEvent()
    data class OnPeriodicityEndChanged(val date: LocalDate) : AppointmentFormEvent()
    data object OnMemoChecked : AppointmentFormEvent()

    // TODO fare gli eventi per notifiche via mail/push notification
    data class OnWarningTimeChanged(val warningTime: Int) : AppointmentFormEvent()
    data class OnWarningUnitChanged(val warningUnit: WarningUnit) : AppointmentFormEvent()
    data class OnSubmit(val onSuccessfullSubmission: () -> Unit) : AppointmentFormEvent()
}