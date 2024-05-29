package com.cosmobile.eplan.core.presentation

import com.cosmobile.eplan.core.util.UiText

sealed class ValidationEvent {
    data object UpdateSuccess : ValidationEvent()
    data object DeleteSuccess : ValidationEvent()
    data class SubmitError(val error: UiText) : ValidationEvent()
    data class RetrieveError(val error: UiText) : ValidationEvent()
    data object NoConnection : ValidationEvent()
}
