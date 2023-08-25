package com.example.eplan.domain.preferences

interface Preferences {
    fun saveToken(token: String)
    fun loadToken(): String?
    fun deleteToken()

    fun saveUsername(username: String)
    fun loadUsername(): String?
    fun deleteUsername()

    fun saveProfilePicUri(uri: String)
    fun loadProfilePicUri(): String?
    fun deleteProfilePicUri()

    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USERNAME = "username"
        const val KEY_PROFILE_PIC_URI = "profile_pic_uri"
    }
}