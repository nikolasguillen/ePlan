package com.cosmobile.eplan.core.domain.preferences

interface Preferences {
    fun saveToken(token: String)
    fun loadToken(): String?
    fun deleteToken()

    fun saveUsername(username: String)
    fun loadUsername(): String?
    fun deleteUsername()

    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USERNAME = "username"
    }
}