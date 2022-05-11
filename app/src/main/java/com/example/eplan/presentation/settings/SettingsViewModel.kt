package com.example.eplan.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
}