package com.example.eplan.presentation.ui.workActivityList

sealed class ActivityListEvent {

    object DayChangeEvent : ActivityListEvent()

    // Ripristino stato app in caso di process death
    object RestoreStateEvent : ActivityListEvent()
}
