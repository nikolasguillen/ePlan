package com.example.eplan.presentation.ui.intervention

import java.time.LocalDate
import java.time.LocalTime

sealed class InterventionFormEvent {
    data class ActivityNameChanged(val name: String) : InterventionFormEvent()
    data class ActivityIdChanged(val id: String) : InterventionFormEvent()
    data class DescriptionChanged(val description: String) : InterventionFormEvent()
    data class DateChanged(val date: LocalDate) : InterventionFormEvent()
    data class StartChanged(val time: LocalTime) : InterventionFormEvent()
    data class EndChanged(val time: LocalTime) : InterventionFormEvent()
    data class MovingTimeChanged(val time: String) : InterventionFormEvent()
    data class KmChanged(val km: String) : InterventionFormEvent()
    object Submit : InterventionFormEvent()
}
