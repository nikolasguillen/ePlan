package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.core.util.toLiteralDateParser
import com.cosmobile.eplan.interventions_list.ui.DisplayMode
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsibleCalendar(
    isCalendarExpanded: Boolean,
    date: LocalDate,
    onToggleCalendarState: () -> Unit,
    onDayChange: (LocalDate) -> Unit,
    onGoToCurrentDate: () -> Unit,
    onIncreaseDate: () -> Unit,
    onDecreaseDate: () -> Unit,
    onToggleListState: () -> Unit,
    onToggleVisualizationMode: () -> Unit,
    isListCollapsed: Boolean,
    displayMode: DisplayMode
) {
    val transition =
        updateTransition(
            targetState = isCalendarExpanded,
            label = "Expand calendar"
        )
    val rotation: Float by transition.animateFloat(label = "Expand calendar") { state ->
        if (state) -180F else 0F
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
        ) {
            Row(modifier = Modifier.weight(2f)) {
                TextButton(
                    onClick = onToggleCalendarState,
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.extraSmall
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                    ) {
                        Text(
                            text = date.dayOfWeek.getDisplayName(
                                java.time.format.TextStyle.FULL,
                                LocalConfiguration.current.locales[0]
                            ).replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = toLiteralDateParser(date = date),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.apri_chiudi_calendario),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .rotate(rotation)
                            .padding(start = MaterialTheme.spacing.extraSmall)
                    )
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(text = stringResource(id = R.string.indietro_data)) } },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        onClick = onDecreaseDate,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.indietro_data),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(text = stringResource(id = R.string.avanti_data)) } },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        onClick = onIncreaseDate,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = stringResource(R.string.avanti_data),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(text = stringResource(id = R.string.vai_a_oggi)) } },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        onClick = onGoToCurrentDate,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Today,
                            contentDescription = stringResource(R.string.vai_a_oggi)
                        )
                    }
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(
                                text = stringResource(
                                    id = when (displayMode) {
                                        DisplayMode.DAILY -> R.string.visualizzazione_giornaliera
                                        DisplayMode.WEEKLY -> R.string.visualizzazione_settimanale
                                    }
                                )
                            )
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        onClick = onToggleVisualizationMode,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = when (displayMode) {
                                DisplayMode.DAILY -> Icons.Default.CalendarViewDay
                                DisplayMode.WEEKLY -> Icons.Default.CalendarViewWeek
                            },
                            contentDescription = stringResource(
                                id = when (displayMode) {
                                    DisplayMode.DAILY -> R.string.visualizzazione_giornaliera
                                    DisplayMode.WEEKLY -> R.string.visualizzazione_settimanale
                                }
                            )
                        )
                    }
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = stringResource(id = if (isListCollapsed) R.string.espandi_lista else R.string.compatta_lista))
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        onClick = onToggleListState,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = if (isListCollapsed) Icons.Default.UnfoldMore else Icons.Default.UnfoldLess,
                            contentDescription = stringResource(id = if (isListCollapsed) R.string.espandi_lista else R.string.compatta_lista)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = isCalendarExpanded) {
            Calendar(
                date = date,
                onDayChange = onDayChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Calendar(
    date: LocalDate,
    onDayChange: (LocalDate) -> Unit
) {
    val calendarState = rememberDatePickerState(
        initialSelectedDateMillis = ZonedDateTime.of(date, LocalTime.MIN, ZoneId.of("UTC"))
            .toEpochSecond() * 1000
    )

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { calendarState.selectedDateMillis }
            .collect {
                it?.let { millis ->
                    val selectedDate = Instant.fromEpochMilliseconds(millis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                        .toJavaLocalDate()

                    if (selectedDate == date) return@collect
                    onDayChange(selectedDate)
                }
            }
    }
    DatePicker(
        state = calendarState,
        showModeToggle = false,
        title = null,
        headline = null,
        modifier = Modifier.fillMaxWidth()
    )
}