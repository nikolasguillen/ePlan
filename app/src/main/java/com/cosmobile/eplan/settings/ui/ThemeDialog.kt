package com.cosmobile.eplan.settings.ui

data class ThemeDialog(
    val themes: List<Pair<ThemeState, Boolean>> = listOf(
        Pair(ThemeState.AutoMode, false),
        Pair(ThemeState.LightMode, false),
        Pair(ThemeState.DarkMode, false)
    )
)