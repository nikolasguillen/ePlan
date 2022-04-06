package com.example.eplan.presentation.ui.workActivity

sealed class ActivityDetailEvent {

    data class GetActivityEvent(
        val id: String
    ) : ActivityDetailEvent()

    object UpdateActivityEvent : ActivityDetailEvent()

    object DeleteActivityEvent : ActivityDetailEvent()
}
