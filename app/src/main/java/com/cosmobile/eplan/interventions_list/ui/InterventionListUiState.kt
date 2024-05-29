package com.cosmobile.eplan.interventions_list.ui

import com.cosmobile.eplan.core.domain.model.WorkActivity
import java.time.LocalDate

data class InterventionListUiState(
    val interventions: Map<String, List<WorkActivity>> = emptyMap(),
    val isRefreshing: Boolean = false,
    val isCalendarExpanded: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val displayMode: DisplayMode = DisplayMode.DAILY,
    val isListCollapsed: Boolean = false,
    val showAbsentConnectionScreen: Boolean = false,
    val isDayCompleted: Boolean = false
)