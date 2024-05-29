package com.cosmobile.eplan.intervention_detail.ui.detail

import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import java.time.LocalDate
import java.time.LocalTime

sealed class InterventionFormEvent : WorkActivityFormEvent {
    data class OnActivityNameChanged(val name: String) : InterventionFormEvent()
    data class OnDescriptionUpdated(val description: String) : InterventionFormEvent()
    data object OnToggleSuggestionsVisibility : InterventionFormEvent()
    data class OnDateChanged(val date: LocalDate) : InterventionFormEvent()
    data class OnStartTimeChanged(val time: LocalTime) : InterventionFormEvent()
    data class OnEndTimeChanged(val time: LocalTime) : InterventionFormEvent()
    data class OnMovingTimeChanged(val time: String) : InterventionFormEvent()
    data class OnKmChanged(val km: String) : InterventionFormEvent()
    data class OnSubmit(val onSuccessfulSubmission: () -> Unit) : InterventionFormEvent()
}
