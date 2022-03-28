package com.example.eplan.presentation.ui.login

sealed class LoginEvent {

    data class LoginAttemptEvent(val username: String, val password: String) : LoginEvent()
}