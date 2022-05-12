package com.example.eplan.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackPressed: () -> Unit
) {

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(id = R.string.impostazioni)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.torna_indietro)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
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
                        text = "Opzioni di visualizzazione",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showThemeDialog = true }) {
                    Text(
                        text = stringResource(R.string.tema),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            start = MaterialTheme.spacing.medium,
                            top = MaterialTheme.spacing.medium,
                            bottom = MaterialTheme.spacing.medium
                        )
                    )
                    Text(
                        text = viewModel.currentThemeMode,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            end = MaterialTheme.spacing.medium,
                            top = MaterialTheme.spacing.medium,
                            bottom = MaterialTheme.spacing.medium
                        )
                    )
                }
            }
        }
    )

    if (showThemeDialog) {
        Dialog(onDismissRequest = { showThemeDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.widthIn(
                    max = LocalConfiguration.current.screenWidthDp.dp.times(0.8F)
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.scegli_tema),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    viewModel.themeStates.forEach {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onTriggerEvent(it.event)
                                    showThemeDialog = false
                                }
                        ) {
                            RadioButton(
                                selected = viewModel.currentThemeMode == it.name,
                                onClick = {
                                    viewModel.onTriggerEvent(it.event)
                                    showThemeDialog = false
                                })
                            Text(text = it.name)
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    ) {
                        TextButton(onClick = { showThemeDialog = false }) {
                            Text(text = stringResource(id = R.string.annulla))
                        }
                    }
                }
            }
        }
    }
}