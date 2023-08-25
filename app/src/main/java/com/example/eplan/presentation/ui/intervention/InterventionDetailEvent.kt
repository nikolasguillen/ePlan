package com.example.eplan.presentation.ui.intervention

sealed class InterventionDetailEvent {

    data class GetInterventionEvent(
        val id: String
    ) : InterventionDetailEvent()
    object UpdateInterventionEvent : InterventionDetailEvent()
    object DeleteInterventionEvent : InterventionDetailEvent()
    object RefreshInterventionEvent : InterventionDetailEvent()
}
