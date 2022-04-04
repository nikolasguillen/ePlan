package com.example.eplan.presentation.ui.components

import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

@Composable
fun CustomSwitch(
    value: MutableState<Boolean> = mutableStateOf(true),
    enabled: MutableState<Boolean> = mutableStateOf(true)
) {
    Switch(
        checked = value.value,
        onCheckedChange = { value.value = it },
        enabled = enabled.value,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface
        )
    )
}