package com.cosmobile.eplan.account

sealed class AccountEvent {
    data class Logout(val onLogout: () -> Unit): AccountEvent()
}
