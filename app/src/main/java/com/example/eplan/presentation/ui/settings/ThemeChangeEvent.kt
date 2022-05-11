package com.example.eplan.presentation.ui.settings

sealed class ThemeChangeEvent {
    object SetLightEvent: ThemeChangeEvent()
    object SetNightEvent: ThemeChangeEvent()
    object FollowSystemThemeEvent: ThemeChangeEvent()
}
