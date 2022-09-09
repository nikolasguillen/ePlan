package com.example.eplan.presentation.ui.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.eplan.presentation.util.THEME_STATE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val themeStates = listOf(ThemeStates.LightMode, ThemeStates.DarkMode, ThemeStates.AutoMode)
    var currentThemeMode = ""
        private set

    init {
        themeStates.forEach {
            if (it.key == getDefaultNightMode()) {
                currentThemeMode = it.name
            }
        }
    }

    fun onTriggerEvent(event: ThemeChangeEvent) {
        when (event) {
            ThemeChangeEvent.FollowSystemThemeEvent -> {
                setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                currentThemeMode = ThemeStates.AutoMode.name
                sharedPreferences.edit().remove(THEME_STATE_KEY).putInt(THEME_STATE_KEY, MODE_NIGHT_FOLLOW_SYSTEM).apply()
            }
            ThemeChangeEvent.SetLightEvent -> {
                setDefaultNightMode(MODE_NIGHT_NO)
                currentThemeMode = ThemeStates.LightMode.name
                sharedPreferences.edit().remove(THEME_STATE_KEY).putInt(THEME_STATE_KEY, MODE_NIGHT_NO).apply()
            }
            ThemeChangeEvent.SetNightEvent -> {
                setDefaultNightMode(MODE_NIGHT_YES)
                currentThemeMode = ThemeStates.DarkMode.name
                sharedPreferences.edit().remove(THEME_STATE_KEY).putInt(THEME_STATE_KEY, MODE_NIGHT_YES).apply()
            }
        }
    }
}