package com.example.eplan.presentation.ui.settings

import androidx.appcompat.app.AppCompatDelegate

sealed class ThemeStates(val name: String, val event: ThemeChangeEvent, val key: Int) {
    object DarkMode: ThemeStates("Scuro", ThemeChangeEvent.SetNightEvent, AppCompatDelegate.MODE_NIGHT_YES)
    object LightMode: ThemeStates("Chiaro", ThemeChangeEvent.SetLightEvent, AppCompatDelegate.MODE_NIGHT_NO)
    object AutoMode: ThemeStates("Predefinito di sistema", ThemeChangeEvent.FollowSystemThemeEvent, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}