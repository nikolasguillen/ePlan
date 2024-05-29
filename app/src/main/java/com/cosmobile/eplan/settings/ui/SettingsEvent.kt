package com.cosmobile.eplan.settings.ui

import java.time.LocalTime

sealed class SettingsEvent {
    data class ChangeThemeEvent(val themeType: ThemeState) : SettingsEvent()
    data object ToggleThemeDialogVisibility : SettingsEvent()
    data class ChangeWorkStartTimeEvent(val startTime: LocalTime) : SettingsEvent()
    data class ChangeWorkEndTimeEvent(val endTime: LocalTime) : SettingsEvent()
    data object ToggleGapsSuggestions : SettingsEvent()
}
