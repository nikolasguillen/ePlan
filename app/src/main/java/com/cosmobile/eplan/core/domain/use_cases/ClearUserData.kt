package com.cosmobile.eplan.core.domain.use_cases

import android.content.SharedPreferences
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.util.SHOW_GAPS_SUGGESTIONS
import com.cosmobile.eplan.core.util.STAY_LOGGED
import com.cosmobile.eplan.core.util.USER_NAME
import com.cosmobile.eplan.core.util.WORK_END_TIME
import com.cosmobile.eplan.core.util.WORK_START_TIME

class ClearUserData(
    private val encryptedPreferences: Preferences,
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(onDataCleared: () -> Unit) {
        sharedPreferences.edit().remove(STAY_LOGGED).apply()
        sharedPreferences.edit().remove(WORK_START_TIME).apply()
        sharedPreferences.edit().remove(WORK_END_TIME).apply()
        sharedPreferences.edit().remove(SHOW_GAPS_SUGGESTIONS).apply()
        sharedPreferences.edit().remove(USER_NAME).apply()
        encryptedPreferences.deleteUsername()
        encryptedPreferences.deleteToken()
        onDataCleared()
    }
}