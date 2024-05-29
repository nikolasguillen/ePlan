package com.cosmobile.eplan.core.presentation.ui

import com.cosmobile.eplan.core.domain.model.Activity

interface WorkActivityFormEvent {
    data class OnActivityQueryChanged(val query: String) : WorkActivityFormEvent
    data class OnActivitySelected(val activity: Activity) : WorkActivityFormEvent
    data object OnToggleActivitySelectorVisibility : WorkActivityFormEvent
}