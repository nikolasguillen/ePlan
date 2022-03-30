package com.example.eplan.presentation.ui.workActivity

import com.example.eplan.domain.model.WorkActivity

sealed class ActivityDetailEvent {

    data class GetActivityEvent(
        val id: String
    ) : ActivityDetailEvent()

    data class UpdateActivityEvent(
        val workActivity: WorkActivity
    ) : ActivityDetailEvent()

    data class DeleteActivityEvent(
        val id: String
    ) : ActivityDetailEvent()
}
