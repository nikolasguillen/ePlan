package com.cosmobile.eplan.appointments_list.ui

import java.time.LocalDate

sealed class AppointmentListEvent {
    data class DayChangeEvent(
        val date: LocalDate? = null,
        val forceRefresh: Boolean = false
    ) : AppointmentListEvent()

    data object OnToggleCalendarState : AppointmentListEvent()
    data object OnToggleListState : AppointmentListEvent()
    data object OnToggleVisualizationMode : AppointmentListEvent()
    data object OnIncreaseDate : AppointmentListEvent()
    data object OnDecreaseDate : AppointmentListEvent()
}