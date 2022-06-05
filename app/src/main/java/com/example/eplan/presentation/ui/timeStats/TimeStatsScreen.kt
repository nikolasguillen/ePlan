package com.example.eplan.presentation.ui.timeStats

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.eplan.R
import com.example.eplan.presentation.ui.timeStats.TimeStatsEvent.GetStats
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.spacing
import com.example.eplan.presentation.util.toLiteralDateParser
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun TimeStatsScreen(
    viewModel: TimeStatsViewModel
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

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
            /*ComposeCalendar(listener = object : SelectDateListener {
                override fun onDateSelected(date: Date) {
                    TODO("Not yet implemented")
                }

                override fun onCanceled() {
                    TODO("Not yet implemented")
                }
            },
            themeColor = MaterialTheme.colorScheme.primary)*/

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
                                it.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
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
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(MaterialTheme.spacing.small)
                                    .weight(0.25F)
                            ) {
                                Text(
                                    text = it.overtime.toString(),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
                                    text = it.vacation.toString(),
                                    color = MaterialTheme.colorScheme.onErrorContainer
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
}
