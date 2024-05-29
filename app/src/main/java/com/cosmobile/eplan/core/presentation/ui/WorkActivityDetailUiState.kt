package com.cosmobile.eplan.core.presentation.ui

import com.cosmobile.eplan.core.domain.model.WorkActivity

interface WorkActivityDetailUiState {
    val workActivity: WorkActivity?
    val showAbsentConnectionScreen: Boolean
    val activitySelectorUiState: ActivitySelectorUiState
    val enableDeletion: Boolean
    fun hasChanged(): Boolean
}