package com.example.eplan.presentation.ui.workActivity

import java.time.LocalTime
import java.util.*

sealed class ActivityFormEvent {
    data class TitleChanged(val title: String): ActivityFormEvent()
    data class DescriptionChanged(val description: String): ActivityFormEvent()
    data class DateChanged(val date: Date): ActivityFormEvent()
    data class StartChanged(val time: LocalTime): ActivityFormEvent()
    data class EndChanged(val time: LocalTime): ActivityFormEvent()
    data class MovingTimeChanged(val time: String): ActivityFormEvent()
    data class KmChanged(val km: String): ActivityFormEvent()
    object Submit: ActivityFormEvent()
}
