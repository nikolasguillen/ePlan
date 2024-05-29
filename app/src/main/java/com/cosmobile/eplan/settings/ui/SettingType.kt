package com.cosmobile.eplan.settings.ui

sealed class SettingType {
    data class BooleanSetting(val value: Boolean) : SettingType()
    data class TextSetting(val value: String) : SettingType()
}