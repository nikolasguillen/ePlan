package com.example.eplan.presentation.ui.account

sealed class AccountEvent {
    object GetUriEvent: AccountEvent()
    object Logout: AccountEvent()
}
