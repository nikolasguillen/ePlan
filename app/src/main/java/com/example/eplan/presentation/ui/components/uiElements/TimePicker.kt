package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.util.fromStringToLocalTime
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun TimePicker(
    startTime: String,
    dialogState: MaterialDialogState,
    onClick: (LocalTime) -> Unit
) {

    androidx.compose.material.MaterialTheme(
        colors = androidx.compose.material.MaterialTheme.colors.copy(
            onBackground = MaterialTheme.colorScheme.onTertiaryContainer,
            background = MaterialTheme.colorScheme.tertiaryContainer,
            primary = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton(
                    text = stringResource(id = R.string.ok_conf),
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
                negativeButton(
                    stringResource(id = R.string.annulla),
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
            },
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            timepicker(
                title = stringResource(R.string.seleziona_orario),
                initialTime = fromStringToLocalTime(startTime),
                is24HourClock = true,
                colors = TimePickerDefaults.colors(
                    activeBackgroundColor = MaterialTheme.colorScheme.primary,
                    activeTextColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                    inactiveTextColor = MaterialTheme.colorScheme.onSurface,
                    selectorColor = MaterialTheme.colorScheme.primary,
                    selectorTextColor = MaterialTheme.colorScheme.onPrimary,
                    headerTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) { time ->
                onClick(time)
            }
        }
    }
}