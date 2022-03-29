package com.example.eplan.presentation.ui.workActivity

import com.example.eplan.domain.model.WorkActivity

sealed class ActivityDetailEvent {

    data class GetActivityByIdEvent(
        val id: Int
    ) : ActivityDetailEvent()

    data class UpdateActivityEvent(
        val workActivity: WorkActivity
    ) : ActivityDetailEvent()

    data class DeleteActivityEvent(
        val id: Int
    ) : ActivityDetailEvent()
}
