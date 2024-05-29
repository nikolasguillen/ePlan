package com.cosmobile.eplan.time_stats.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.presentation.ui.components.placeholders.AbsentConnectionPlaceholder
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.MonthPicker
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.time_stats.ui.TimeStatsEvent.RefreshStats
import com.cosmobile.eplan.time_stats.ui.TimeStatsEvent.UpdateDate
import com.cosmobile.eplan.time_stats.ui.components.StatsCardPlaceholder
import com.cosmobile.eplan.time_stats.ui.components.StatsHeader
import com.cosmobile.eplan.time_stats.ui.components.TableLegend
import com.cosmobile.eplan.time_stats.ui.components.WeekStatsCard
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun TimeStatsScreen(
    state: TImeStatsUiState,
    onEvent: (TimeStatsEvent) -> Unit,
    validationEventsFlow: Flow<String>,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var showMonthPicker by remember { mutableStateOf(false) }
    var showLegendDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState {
        state.stats.size
    }

    LaunchedEffect(key1 = context) {
        validationEventsFlow.collect { error ->
            snackBarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.statistiche_ore)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLegendDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.apri_legenda)
                        )
                    }
                    IconButton(onClick = { onEvent(RefreshStats) }) {
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
            StatsHeader(
                currentDate = "${
                    state.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        .replaceFirstChar { it.uppercase() }
                } ${state.year}",
                totalHours = stringResource(
                    id = R.string.totale_ore,
                    state.stats.sumOf { it.getTotalHours() }.toString()
                ),
                standardHours = stringResource(
                    id = R.string.ore_normali,
                    state.stats.sumOf { it.getTotalStandardTime() }.toString()
                ),
                overtimeHours = stringResource(
                    id = R.string.ore_straordinarie,
                    state.stats.sumOf { it.getTotalOvertime() }.toString()
                ),
                vacationHours = stringResource(
                    id = R.string.ferie,
                    state.stats.sumOf { it.getTotalVacation() }.toString()
                ),
                diseaseHours = stringResource(
                    id = R.string.malattia,
                    state.stats.sumOf { it.getTotalDisease() }.toString()
                ),
                onDateButtonClick = { showMonthPicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.small),
                shouldShowPlaceholders = state.loading
            )


            Crossfade(
                targetState = state.loading,
                label = "StatsContentCrossfade",
                modifier = Modifier.weight(1f)
            ) { loading ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (loading) {
                        StatsCardPlaceholder(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .padding(horizontal = MaterialTheme.spacing.large)
                        )
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.large),
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            state.stats.getOrNull(page)?.let {
                                WeekStatsCard(
                                    weekStats = it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.small)
                                        .graphicsLayer {
                                            val pageOffSet = (
                                                    (pagerState.currentPage - page) + pagerState
                                                        .currentPageOffsetFraction
                                                    ).absoluteValue
                                            alpha = lerp(
                                                start = 0.5f,
                                                stop = 1f,
                                                fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                                            )
                                            scaleY = lerp(
                                                start = 0.75f,
                                                stop = 1f,
                                                fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.showAbsentConnectionScreen && state.loading.not()) {
            AbsentConnectionPlaceholder(
                onRetry = { onEvent(RefreshStats) },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }

        if (showMonthPicker) {
            Dialog(onDismissRequest = { showMonthPicker = false }) {
                MonthPicker(
                    startMonth = Month(state.month.value),
                    startYear = state.year,
                    onConfirm = { month, year ->
                        onEvent(UpdateDate(month = month, year = year))
                        showMonthPicker = false
                    },
                    onDismiss = { showMonthPicker = false },
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
                title = { Text(text = stringResource(id = R.string.legenda)) },
                text = { TableLegend(modifier = Modifier.fillMaxWidth()) }
            )
        }
    }
}