package com.example.eplan.domain.preferences

interface Preferences {
    fun saveToken(token: String)
    fun loadToken(): String?
    fun deleteToken()

    fun saveShouldShowLogin(shouldShowLogin: Boolean)
    fun loadShouldShowLogin(): Boolean
    fun deleteShouldShowLogin()

    fun saveUsername(username: String)
    fun loadUsername(): String?
    fun deleteUsername()

    fun saveProfilePicUri(uri: String)
    fun loadProfilePicUri(): String?
    fun deleteProfilePicUri()

    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_SHOULD_SHOW_LOGIN = "should_show_login"
        const val KEY_USERNAME = "username"
        const val KEY_PROFILE_PIC_URI = "profile_pic_uri"
    }
}