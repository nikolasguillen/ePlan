package com.cosmobile.eplan.core.presentation.ui

import com.cosmobile.eplan.core.domain.model.Activity

data class ActivitySelectorUiState(
    val activities: List<Activity> = emptyList(),
    val searchQuery: String = "",
    val isVisible: Boolean = false
)
