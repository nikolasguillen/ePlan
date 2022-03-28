package com.example.eplan.presentation.ui.workActivityList

sealed class ActivityListEvent {

    data class DayChangeEvent(
        val dayOfMonth: Int,
        val month: Int,
        val year: Int
    ) : ActivityListEvent()
}
