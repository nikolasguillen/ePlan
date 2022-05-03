package com.example.eplan.presentation.ui

sealed class ValidationEvent {
    object UpdateSuccess : ValidationEvent()
    data class SubmitError(val error: String) : ValidationEvent()
    data class RetrieveError(val error: String) : ValidationEvent()
}
