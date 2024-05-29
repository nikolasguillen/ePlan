package com.cosmobile.eplan.settings.ui

import java.time.LocalTime

data class SettingsUiState(
    val currentTheme: ThemeState = ThemeState.AutoMode,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(18, 0),
    val enableSuggestions: Boolean = true,
    val themeDialog: ThemeDialog? = null
)