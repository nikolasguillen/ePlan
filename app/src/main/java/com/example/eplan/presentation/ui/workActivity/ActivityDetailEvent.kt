package com.example.eplan.presentation.ui.workActivity

import com.example.eplan.domain.model.WorkActivity

sealed class ActivityDetailEvent {

    data class GetActivityEvent(
        val id: String
    ) : ActivityDetailEvent()

    object UpdateActivityEvent : ActivityDetailEvent()

    object DeleteActivityEvent : ActivityDetailEvent()
}
