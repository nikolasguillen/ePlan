package com.cosmobile.eplan.appointments_list.ui

import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.interventions_list.ui.DisplayMode
import java.time.LocalDate

data class AppointmentListUiState(
    val appointments: Map<String, List<WorkActivity>> = emptyMap(),
    val isRefreshing: Boolean = false,
    val isCalendarExpanded: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val isListCollapsed: Boolean = false,
    val showAbsentConnectionScreen: Boolean = false,
    val displayMode: DisplayMode = DisplayMode.DAILY,
    val isDayCompleted: Boolean = false
)
