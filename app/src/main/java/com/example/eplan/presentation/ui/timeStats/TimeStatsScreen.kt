package com.example.eplan.presentation.ui.timeStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.dt.composedatepicker.ComposeCalendar
import com.dt.composedatepicker.SelectDateListener
import com.example.eplan.R
import com.example.eplan.presentation.ui.components.CustomDialog
import com.example.eplan.presentation.ui.timeStats.TimeStatsEvent.GetStats
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.spacing
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun TimeStatsScreen(
    viewModel: TimeStatsViewModel
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { error ->
            snackBarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Statistiche ore") },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onTriggerEvent(GetStats) }) {
                        Icon(
                            imageVector = Icons.Filled.Autorenew,
                            contentDescription = stringResource(id = R.string.aggiorna)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = MaterialTheme.spacing.medium)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                TextButton(onClick = { showDialog = true }) {
                    Text(text = "${
                        viewModel.date.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
                            .replaceFirstChar { it.uppercase() }
                    } ${viewModel.date.year}",
                    style = MaterialTheme.typography.headlineMedium)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.stats) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${
                                it.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                                    .replaceFirstChar { it.uppercase() }
                            } ${it.date.dayOfMonth}",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1F)
                        )
                        Row(modifier = Modifier.weight(1F)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(MaterialTheme.spacing.small)
                                    .weight(0.25F)
                            ) {
                                Text(
                                    text = it.standardTime.toString(),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.errorContainer)
                                    .padding(MaterialTheme.spacing.small)
                                    .weight(0.25F)
                            ) {
                                Text(
                                    text = it.overtime.toString(),
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(MaterialTheme.spacing.small)
                                    .weight(0.25F)
                            ) {
                                Text(
                                    text = it.vacation.toString(),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                                    .padding(MaterialTheme.spacing.small)
                                    .weight(0.25F)
                            ) {
                                Text(
                                    text = it.disease.toString(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F),
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
                    )
                }
            }
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            ComposeCalendar(
                positiveButtonTitle = stringResource(id = R.string.conferma),
                negativeButtonTitle = stringResource(id = R.string.annulla),
                listener = object : SelectDateListener {
                    override fun onDateSelected(date: Date) {
                        viewModel.date = fromDateToLocalDate(date)
                        showDialog = false
                    }

                    override fun onCanceled() {
                        showDialog = false
                    }
                },
                themeColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}
