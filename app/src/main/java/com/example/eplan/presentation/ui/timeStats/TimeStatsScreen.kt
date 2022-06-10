package com.example.eplan.presentation.ui.timeStats

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
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
                    .padding(
                        horizontal = MaterialTheme.spacing.medium
                    )
            ) {
                Box(modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable { showDialog = true }
                ) {
                    Text(
                        text = "${
                            viewModel.date.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
                                .replaceFirstChar { it.uppercase() }
                        } ${viewModel.date.year}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(MaterialTheme.spacing.small)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {

                }
                items(viewModel.stats) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = it.date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = it.date.month.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ITALIAN
                                ).uppercase(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            text = it.date.dayOfWeek.getDisplayName(
                                TextStyle.SHORT,
                                Locale.ITALIAN
                            ).uppercase(),
                            style = MaterialTheme.typography.labelLarge

                        )
                        Text(
                            text = it.standardTime.toString()
                        )
                        Text(
                            text = it.overtime.toString()
                        )
                        Text(
                            text = it.vacation.toString()
                        )
                        Text(
                            text = it.disease.toString()
                        )
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
