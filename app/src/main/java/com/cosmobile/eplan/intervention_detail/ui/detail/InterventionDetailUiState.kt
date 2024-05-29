package com.cosmobile.eplan.intervention_detail.ui.detail

import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.presentation.ui.ActivitySelectorUiState
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailUiState

data class InterventionDetailUiState(
    override val workActivity: Intervention? = null,
    override val showAbsentConnectionScreen: Boolean = false,
    override val activitySelectorUiState: ActivitySelectorUiState = ActivitySelectorUiState(),
    override val enableDeletion: Boolean = false,
    val initialInterventionState: Intervention? = null,
    val descriptionSuggestions: List<String> = emptyList(),
    val showDescriptionSuggestions: Boolean = false
) : WorkActivityDetailUiState {
    override fun hasChanged(): Boolean {
        return initialInterventionState != workActivity
    }
}


