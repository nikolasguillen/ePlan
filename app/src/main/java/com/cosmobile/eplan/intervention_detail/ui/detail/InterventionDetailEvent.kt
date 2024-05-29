package com.cosmobile.eplan.intervention_detail.ui.detail

import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailEvent

sealed class InterventionDetailEvent : WorkActivityDetailEvent {
    data class GetInterventionEvent(
        val id: String
    ) : InterventionDetailEvent()

    data object UpdateInterventionEvent : InterventionDetailEvent()
    data object DeleteInterventionEvent : InterventionDetailEvent()
    data object RefreshInterventionEvent : InterventionDetailEvent()
}
