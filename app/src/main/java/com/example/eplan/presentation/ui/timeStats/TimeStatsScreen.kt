package com.example.eplan.presentation.ui.timeStats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dt.composedatepicker.ComposeCalendar
import com.dt.composedatepicker.SelectDateListener
import com.example.eplan.R
import com.example.eplan.presentation.ui.timeStats.TimeStatsEvent.GetStats
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.spacing
import java.time.format.TextStyle
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun TimeStatsScreen(
    viewModel: TimeStatsViewModel,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var showMonthPicker by remember { mutableStateOf(false) }
    var showLegendDialog by remember { mutableStateOf(false) }

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
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLegendDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.legenda)
                        )
                    }
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                Box(modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable { showMonthPicker = true }
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
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
            ) {
                items(viewModel.getWeekSortedStats()) { statsPairs ->
                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.small)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Settimana ${statsPairs.first}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(MaterialTheme.spacing.medium)
                            )
                        }
                        Divider(color = MaterialTheme.colorScheme.surface, thickness = 1.dp)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(
                                    vertical = MaterialTheme.spacing.medium,
                                    horizontal = MaterialTheme.spacing.extraSmall
                                )
                        ) {
                            viewModel.columnHeaders.keys.forEach {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        statsPairs.second.forEach {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.extraSmall)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = it.date.dayOfMonth.toString(),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = it.date.month.getDisplayName(
                                                TextStyle.SHORT,
                                                Locale.getDefault()
                                            ).uppercase(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.date.dayOfWeek.getDisplayName(
                                            TextStyle.SHORT,
                                            Locale.getDefault()
                                        ).uppercase(),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.standardTime.toString(),
                                        color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        fontWeight = if (it.standardTime > 0.0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.overtime.toString(),
                                        color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        fontWeight = if (it.overtime > 0.0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.vacation.toString(),
                                        color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        fontWeight = if (it.vacation > 0.0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.disease.toString(),
                                        color = if (viewModel.checkMinimumTime(it.date)) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        fontWeight = if (it.disease > 0.0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            if (!viewModel.checkMinimumTime(it.date)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = MaterialTheme.spacing.extraSmall,
                                            horizontal = MaterialTheme.spacing.small
                                        )
                                ) {
                                    Text(
                                        text = "Ricontrolla le ore!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.error,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                            if (statsPairs.second.indexOf(it) != statsPairs.second.lastIndex) {
                                Divider(
                                    thickness = 1.dp,
                                    color = Color.Black.copy(alpha = 0.2F),
                                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiary)
                                .padding(MaterialTheme.spacing.small)
                        ) {
                            Text(
                                text = "Bilancio settimanale: ${
                                    viewModel.getWeekTotalHours(
                                        statsPairs.first
                                    )
                                } ore",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onTertiary,
                                maxLines = 1
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
            }
        }
        if (showMonthPicker) {
            Dialog(onDismissRequest = { showMonthPicker = false }) {
                ComposeCalendar(
                    positiveButtonTitle = stringResource(id = R.string.conferma),
                    negativeButtonTitle = stringResource(id = R.string.annulla),
                    listener = object : SelectDateListener {
                        override fun onDateSelected(date: Date) {
                            viewModel.date = fromDateToLocalDate(date)
                            showMonthPicker = false
                        }

                        override fun onCanceled() {
                            showMonthPicker = false
                        }
                    },
                    themeColor = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (showLegendDialog) {
            AlertDialog(
                onDismissRequest = { showLegendDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showLegendDialog = false }) {
                        Text(text = stringResource(id = R.string.ok_conf))
                    }
                },
                title = { Text(text = "Legenda") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.columnHeaders.forEach {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "• ${it.key}: ", fontWeight = FontWeight.Bold)
                                Text(text = it.value)
                            }
                        }
                    }
                }
            )
        }
    }
}
