package com.cosmobile.eplan.interventions_list.ui

import java.time.LocalDate

sealed class InterventionListEvent {
    data class DayChangeEvent(val date: LocalDate? = null, val forceRefresh: Boolean = false) : InterventionListEvent()
    data object OnToggleCalendarState : InterventionListEvent()
    data object OnToggleListState : InterventionListEvent()
    data object OnToggleVisualizationMode : InterventionListEvent()
    data object OnIncreaseDate : InterventionListEvent()
    data object OnDecreaseDate : InterventionListEvent()
}
