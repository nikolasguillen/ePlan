package com.example.eplan.presentation.ui.workActivityList

sealed class ActivityListEvent {

    data class DayChangeEvent(
        val date: String
    ) : ActivityListEvent()
}
