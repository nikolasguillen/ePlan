package com.example.eplan.presentation.ui.workActivity

import com.example.eplan.domain.model.WorkActivity

sealed class ActivityDetailEvent {

    data class GetActivityByIdEvent(
        val id: Int
    ) : ActivityDetailEvent()

    data class UpdateActivityDetailEvent(
        val workActivity: WorkActivity
    ) : ActivityDetailEvent()
}
