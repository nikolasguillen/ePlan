package com.example.eplan.presentation.ui.login

sealed class LoginEvent {

    object LoginAttemptEvent : LoginEvent()
}