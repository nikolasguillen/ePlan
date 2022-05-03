package com.example.eplan.presentation.ui.interventionList

sealed class InterventionListEvent {

    data class DayChangeEvent(
        val date: String
    ) : InterventionListEvent()
}
