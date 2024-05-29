package com.cosmobile.eplan.auth.ui

import android.app.Activity

sealed class AuthEvent {
    data class OnUsernameChange(val value: String) : AuthEvent()
    data class OnPasswordChange(val value: String) : AuthEvent()
    data class OnLogin(val activity: Activity) : AuthEvent()
    data object OnStayLoggedInClick : AuthEvent()
    data class TryAutoLogin(
        val activity: Activity,
        val onSuccess: () -> Unit
    ) : AuthEvent()
}