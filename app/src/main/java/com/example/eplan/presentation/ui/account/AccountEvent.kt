package com.example.eplan.presentation.ui.account

sealed class AccountEvent {
    data object GetUriEvent: AccountEvent()
    data class Logout(val onLogout: () -> Unit): AccountEvent()
}
