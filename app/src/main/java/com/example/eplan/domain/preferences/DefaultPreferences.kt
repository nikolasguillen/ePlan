package com.example.eplan.domain.preferences

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

    override fun saveShouldShowLogin(shouldShowLogin: Boolean) {
        encryptedSharedPref.edit()
            .putBoolean(Preferences.KEY_SHOULD_SHOW_LOGIN, shouldShowLogin)
            .apply()
    }

    override fun loadShouldShowLogin(): Boolean {
        return encryptedSharedPref.getBoolean(Preferences.KEY_SHOULD_SHOW_LOGIN, true)
    }

    override fun deleteShouldShowLogin() {
        encryptedSharedPref.edit()
            .remove(Preferences.KEY_SHOULD_SHOW_LOGIN)
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

    override fun saveProfilePicUri(uri: String) {
        encryptedSharedPref.edit()
            .putString(Preferences.KEY_PROFILE_PIC_URI, uri)
            .apply()
    }

    override fun loadProfilePicUri(): String? {
        return encryptedSharedPref.getString(Preferences.KEY_PROFILE_PIC_URI, null)
    }

    override fun deleteProfilePicUri() {
        encryptedSharedPref.edit()
            .remove(Preferences.KEY_PROFILE_PIC_URI)
            .apply()
    }


}