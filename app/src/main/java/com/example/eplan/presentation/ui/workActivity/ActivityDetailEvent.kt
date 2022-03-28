package com.example.eplan.presentation.ui.workActivity

sealed class ActivityDetailEvent {
    data class GetActivityById(
        val id: Int
    ): ActivityDetailEvent()
}
