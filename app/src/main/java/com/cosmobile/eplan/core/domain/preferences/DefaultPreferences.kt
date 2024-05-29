package com.cosmobile.eplan.core.domain.preferences

import android.content.SharedPreferences

class DefaultPreferences(
    private val encryptedSharedPref: SharedPreferences
) : Preferences {
    override fun saveToken(token: String) {
        encryptedSharedPref.edit()
            .putString(Preferences.KEY_TOKEN, token)
            .apply()
    }

    override fun loadToken(): String? {
        return encryptedSharedPref.getString(Preferences.KEY_TOKEN, null)
    }

    override fun deleteToken() {
        encryptedSharedPref.edit()
            .remove(Preferences.KEY_TOKEN)
            .apply()
    }

    override fun saveUsername(username: String) {
        encryptedSharedPref.edit()
            .putString(Preferences.KEY_USERNAME, username)
            .apply()
    }

    override fun loadUsername(): String? {
        return encryptedSharedPref.getString(Preferences.KEY_USERNAME, null)
    }

    override fun deleteUsername() {
        encryptedSharedPref.edit()
            .remove(Preferences.KEY_USERNAME)
            .apply()
    }
}