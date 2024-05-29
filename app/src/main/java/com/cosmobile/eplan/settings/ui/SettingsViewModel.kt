package com.cosmobile.eplan.settings.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModel
import com.cosmobile.eplan.core.util.SHOW_GAPS_SUGGESTIONS
import com.cosmobile.eplan.core.util.THEME_STATE_KEY
import com.cosmobile.eplan.core.util.WORK_END_TIME
import com.cosmobile.eplan.core.util.WORK_START_TIME
import com.cosmobile.eplan.settings.ui.SettingsEvent.ChangeThemeEvent
import com.cosmobile.eplan.settings.ui.SettingsEvent.ChangeWorkEndTimeEvent
import com.cosmobile.eplan.settings.ui.SettingsEvent.ChangeWorkStartTimeEvent
import com.cosmobile.eplan.settings.ui.SettingsEvent.ToggleGapsSuggestions
import com.cosmobile.eplan.settings.ui.SettingsEvent.ToggleThemeDialogVisibility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    init {
        val currentTheme = when (getDefaultNightMode()) {
            MODE_NIGHT_NO -> ThemeState.LightMode
            MODE_NIGHT_YES -> ThemeState.DarkMode
            MODE_NIGHT_FOLLOW_SYSTEM -> ThemeState.AutoMode
            else -> ThemeState.AutoMode
        }
        val workStartTime = sharedPreferences.getString(WORK_START_TIME, "09:00")?.run {
            LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
        } ?: LocalTime.of(9, 0)
        val workEndTime = sharedPreferences.getString(WORK_END_TIME, "18:00")?.run {
            LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
        } ?: LocalTime.of(18, 0)
        val enableSuggestions = sharedPreferences.getBoolean(SHOW_GAPS_SUGGESTIONS, true)

        _state.update {
            it.copy(
                currentTheme = currentTheme,
                startTime = workStartTime,
                endTime = workEndTime,
                enableSuggestions = enableSuggestions
            )
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is ChangeThemeEvent -> {
                when (event.themeType) {
                    ThemeState.LightMode -> {
                        setDefaultNightMode(MODE_NIGHT_NO)
                        _state.update { it.copy(currentTheme = ThemeState.LightMode) }
                        sharedPreferences.edit().remove(THEME_STATE_KEY)
                            .putInt(THEME_STATE_KEY, MODE_NIGHT_NO).apply()
                    }

                    ThemeState.DarkMode -> {
                        setDefaultNightMode(MODE_NIGHT_YES)
                        _state.update { it.copy(currentTheme = ThemeState.DarkMode) }
                        sharedPreferences.edit().remove(THEME_STATE_KEY)
                            .putInt(THEME_STATE_KEY, MODE_NIGHT_YES).apply()
                    }

                    ThemeState.AutoMode -> {
                        setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                        _state.update { it.copy(currentTheme = ThemeState.AutoMode) }
                        sharedPreferences.edit().remove(THEME_STATE_KEY)
                            .putInt(THEME_STATE_KEY, MODE_NIGHT_FOLLOW_SYSTEM).apply()
                    }
                }

                onEvent(ToggleThemeDialogVisibility)
            }

            is ToggleThemeDialogVisibility -> {
                val themeDialog = ThemeDialog().run {
                    this.copy(
                        themes = this.themes.map {
                            it.copy(second = it.first == state.value.currentTheme)
                        }
                    )
                }
                _state.update {
                    it.copy(themeDialog = if (it.themeDialog == null) themeDialog else null)
                }
            }

            is ChangeWorkStartTimeEvent -> {
                sharedPreferences.edit().remove(WORK_START_TIME)
                    .putString(WORK_START_TIME, event.startTime.toString()).apply()
                _state.update { it.copy(startTime = event.startTime) }
            }

            is ChangeWorkEndTimeEvent -> {
                sharedPreferences.edit().remove(WORK_END_TIME)
                    .putString(WORK_END_TIME, event.endTime.toString()).apply()
                _state.update { it.copy(endTime = event.endTime) }
            }

            is ToggleGapsSuggestions -> {
                sharedPreferences.edit().remove(SHOW_GAPS_SUGGESTIONS)
                    .putBoolean(SHOW_GAPS_SUGGESTIONS, !state.value.enableSuggestions).apply()
                _state.update { it.copy(enableSuggestions = !state.value.enableSuggestions) }
            }
        }
    }
}