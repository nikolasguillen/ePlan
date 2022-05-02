package com.example.eplan.presentation.ui.components

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.presentation.util.fromLocalDateToDate
import com.example.eplan.presentation.util.spacing
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate

@Composable
fun CollapsibleCalendar(
    calendarVisibility: MutableState<Boolean>,
    date: String,
    onDayChange: (String) -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toLiteralDateParser(date = date),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { calendarVisibility.value = !calendarVisibility.value }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Apri o chiudi il calendario",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
            IconButton(onClick = { onDayChange(LocalDate.now().toString()) }) {
                Icon(imageVector = Icons.Filled.Today, contentDescription = "Val alla data di oggi")
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