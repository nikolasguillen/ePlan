package com.cosmobile.eplan.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.UiText

sealed class ThemeState(val name: UiText, val key: Int) {
    data object DarkMode :
        ThemeState(
            name = UiText.StringResource(R.string.tema_scuro),
            key = AppCompatDelegate.MODE_NIGHT_YES
        )

    data object LightMode :
        ThemeState(
            name = UiText.StringResource(R.string.tema_chiaro),
            key = AppCompatDelegate.MODE_NIGHT_NO
        )

    data object AutoMode :
        ThemeState(
            name = UiText.StringResource(R.string.tema_auto),
            key = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
}