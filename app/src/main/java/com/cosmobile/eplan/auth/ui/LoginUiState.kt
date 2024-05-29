package com.cosmobile.eplan.auth.ui

import com.cosmobile.eplan.core.util.UiText

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: UiText? = null,
    val passwordError: UiText? = null,
    val errorMessage: UiText? = null,
    val stayLogged: Boolean = true,
    val loading: Boolean = false,
    val showScreen: Boolean = false
)