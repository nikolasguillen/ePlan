package com.cosmobile.eplan.settings.ui

data class SettingsRowState(
    val settingType: SettingType,
    val title: String,
    val description: String? = null,
    val onClick: () -> Unit
)