package com.example.eplan.presentation.ui.appointment

import com.example.eplan.domain.model.User
import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
import java.time.LocalDate
import java.time.LocalTime

sealed class AppointmentFormEvent {
    data class ActivityNameChanged(val name: String) : AppointmentFormEvent()
    data class ActivityIdChanged(val id: String) : AppointmentFormEvent()
    data class DescriptionChanged(val description: String) : AppointmentFormEvent()
    data class DateChanged(val date: LocalDate) : AppointmentFormEvent()
    data class StartChanged(val time: LocalTime) : AppointmentFormEvent()
    data class EndChanged(val time: LocalTime) : AppointmentFormEvent()
    data class PlanningChanged(val selected: Boolean) : AppointmentFormEvent()
    data class InterventionChanged(val selected: Boolean) : AppointmentFormEvent()
    data class AddInvited(val invited: User) : AppointmentFormEvent()
    data class RemoveInvited(val invited: User) : AppointmentFormEvent()
    object ConfirmInvitedList : AppointmentFormEvent()
    object DismissInvitedList : AppointmentFormEvent()
    data class PeriodicityChanged(val periodicity: Periodicity) : AppointmentFormEvent()
    data class PeriodicityEndChanged(val date: LocalDate) : AppointmentFormEvent()
    data class MemoChanged(val selected: Boolean) : AppointmentFormEvent()

    // TODO fare gli eventi per notifiche via mail/push notification
    data class WarningTimeChanged(val warningTime: Int) : AppointmentFormEvent()
    data class WarningUnitChanged(val warningUnit: WarningUnit) : AppointmentFormEvent()
    object Submit : AppointmentFormEvent()
}