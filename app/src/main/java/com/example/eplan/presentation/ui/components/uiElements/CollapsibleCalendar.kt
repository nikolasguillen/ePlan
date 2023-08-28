package com.example.eplan.presentation.ui.components

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.example.eplan.presentation.util.fromLocalDateToDate
import com.example.eplan.presentation.util.spacing
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsibleCalendar(
    calendarVisibility: MutableState<Boolean>,
    date: String,
    onDayChange: (String) -> Unit,
    onCollapseList: () -> Unit,
    isListCollapsed: Boolean
) {
    val transition =
        updateTransition(
            targetState = calendarVisibility.value,
            label = "Expand calendar"
        )
    val rotation: Float by transition.animateFloat(label = "Expand calendar") { state ->
        if (state) -180F else 0F
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.extraSmall
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { calendarVisibility.value = !calendarVisibility.value }) {
                Text(
                    text = toLiteralDateParser(date = date),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.apri_chiudi_calendario),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(rotation)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                PlainTooltipBox(
                    tooltip = { Text(text = stringResource(id = R.string.vai_a_oggi)) }
                ) {
                    IconButton(
                        onClick = { onDayChange(LocalDate.now().toString()) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.tooltipTrigger()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Today,
                            contentDescription = stringResource(R.string.vai_a_oggi)
                        )
                    }
                }
                PlainTooltipBox(
                    tooltip = { Text(text = stringResource(id = if (isListCollapsed) R.string.espandi_lista else R.string.compatta_lista)) }
                ) {
                    IconButton(
                        onClick = { onCollapseList() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.tooltipTrigger()
                    ) {
                        Icon(
                            imageVector = if (isListCollapsed) Icons.Default.UnfoldLess else Icons.Default.UnfoldMore,
                            contentDescription = stringResource(id = if (isListCollapsed) R.string.espandi_lista else R.string.compatta_lista)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = calendarVisibility.value) {
            Calendar(
                date = date,
                onDayChange = onDayChange
            )
        }
    }
}

@Composable
private fun Calendar(
    date: String,
    onDayChange: (String) -> Unit
) {
    AndroidView(
        factory = { context ->
            CalendarView(context).apply {

                this.date = fromLocalDateToDate(LocalDate.parse(date)).time

                this.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    this.date = fromLocalDateToDate(selectedDate).time
                    onDayChange(selectedDate.toString())
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}