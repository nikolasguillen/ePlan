package com.cosmobile.eplan.auth.domain.use_case

import android.content.SharedPreferences
import com.cosmobile.eplan.core.util.USER_NAME
import com.cosmobile.eplan.core.util.WORK_END_TIME
import com.cosmobile.eplan.core.util.WORK_START_TIME
import java.time.LocalTime
import javax.inject.Inject

class SaveUserData
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun execute(name: String, workingHours: String?) {
        val convertedHours = workingHours?.toIntOrNull() ?: 8
        sharedPreferences.edit().putString(USER_NAME, name).apply()
        val startTime = LocalTime.of(9, 0)
        val endTime = startTime.plusHours(convertedHours.toLong() + 1)
        sharedPreferences.edit().putString(WORK_START_TIME, startTime.toString()).apply()
        sharedPreferences.edit().putString(WORK_END_TIME, endTime.toString()).apply()
    }
}