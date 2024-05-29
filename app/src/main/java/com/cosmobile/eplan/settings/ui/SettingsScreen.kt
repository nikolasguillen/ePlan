package com.cosmobile.eplan.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomDialog
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.TimePickerDialog
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.settings.ui.SettingType.BooleanSetting
import com.cosmobile.eplan.settings.ui.SettingType.TextSetting
import com.cosmobile.eplan.settings.ui.SettingsEvent.ChangeThemeEvent
import com.cosmobile.eplan.settings.ui.SettingsEvent.ToggleThemeDialogVisibility
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var showStartTimeDialog by remember { mutableStateOf(false) }
    var showEndTimeDialog by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState(
        initialHour = state.startTime.hour,
        initialMinute = state.startTime.minute
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = state.endTime.hour,
        initialMinute = state.endTime.minute
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.impostazioni)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.torna_indietro)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {

            SettingsSection(
                title = stringResource(R.string.opzioni_visualizzazione),
                items = listOf(
                    SettingsRowState(
                        title = stringResource(R.string.tema),
                        settingType = TextSetting(
                            state.currentTheme.name.asString(
                                context
                            )
                        ),
                        onClick = { onEvent(ToggleThemeDialogVisibility) }
                    )
                )
            )

            SettingsSection(
                title = stringResource(R.string.orario_lavorativo),
                items = listOf(
                    SettingsRowState(
                        title = stringResource(R.string.orario_inizio),
                        settingType = TextSetting(state.startTime.toString()),
                        onClick = { showStartTimeDialog = true }
                    ),
                    SettingsRowState(
                        title = stringResource(R.string.orario_fine),
                        settingType = TextSetting(state.endTime.toString()),
                        onClick = { showEndTimeDialog = true }
                    ),
                    SettingsRowState(
                        title = stringResource(R.string.suggerimenti_fasce_orarie),
                        description = stringResource(R.string.suggerimenti_fasce_orarie_descrizione),
                        settingType = BooleanSetting(state.enableSuggestions),
                        onClick = { onEvent(SettingsEvent.ToggleGapsSuggestions) }
                    )
                )
            )
        }
    }

    if (state.themeDialog != null) {
        CustomDialog(
            title = stringResource(id = R.string.scegli_tema),
            onDismissRequest = { onEvent(ToggleThemeDialogVisibility) },
        ) {
            LazyColumn {
                items(state.themeDialog.themes) { (theme, isSelected) ->
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEvent(ChangeThemeEvent(theme)) }
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onEvent(ChangeThemeEvent(theme)) })
                        Text(text = theme.name.asString(context))
                    }
                }
            }
        }
    }

    if (showStartTimeDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.seleziona_orario),
            onCancel = { showStartTimeDialog = false },
            onConfirm = {
                onEvent(
                    SettingsEvent.ChangeWorkStartTimeEvent(
                        LocalTime.of(startTimePickerState.hour, startTimePickerState.minute)
                    )
                )
                showStartTimeDialog = false
            }
        ) {
            TimePicker(state = startTimePickerState)
        }
    }

    if (showEndTimeDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.seleziona_orario),
            onCancel = { showEndTimeDialog = false },
            onConfirm = {
                onEvent(
                    SettingsEvent.ChangeWorkEndTimeEvent(
                        LocalTime.of(endTimePickerState.hour, endTimePickerState.minute)
                    )
                )
                showEndTimeDialog = false
            }
        ) {
            TimePicker(state = endTimePickerState)
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingsRowState>
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items.forEach { (settingType, title, description, action) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { action() }
            ) {
                Column(modifier = Modifier.weight(8f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            start = MaterialTheme.spacing.medium,
                            top = MaterialTheme.spacing.medium,
                            bottom = if (description == null) MaterialTheme.spacing.medium else MaterialTheme.spacing.extraSmall
                        )
                    )

                    description?.let {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(
                                start = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium
                            )
                        )
                    }
                }

                when (settingType) {
                    is BooleanSetting -> {
                        Switch(
                            checked = settingType.value,
                            onCheckedChange = { action() },
                            modifier = Modifier.weight(2f)
                        )
                    }

                    is TextSetting -> {
                        Text(
                            text = settingType.value,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(
                                    end = MaterialTheme.spacing.medium,
                                    top = MaterialTheme.spacing.medium,
                                    bottom = MaterialTheme.spacing.medium
                                )
                        )
                    }
                }
            }
        }
    }
}