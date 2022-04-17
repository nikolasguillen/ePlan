package com.example.eplan.presentation.ui.components

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent
import com.example.eplan.presentation.util.fromLocalDateToDate
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate

@Composable
fun CollapsibleCalendar(
    calendarVisibility: MutableState<Boolean>,
    date: String,
    onDayChange: (String) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { calendarVisibility.value = !calendarVisibility.value }
                .padding(16.dp)
        ) {
            Text(
                text = toLiteralDateParser(date = date),
                style = MaterialTheme.typography.titleMedium
            )
            val transition =
                updateTransition(
                    targetState = calendarVisibility.value,
                    label = "Expand calendar"
                )
            val rotation: Float by transition.animateFloat(label = "Expand calendar") { state ->
                if (state) -180F else 0F
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Apri o chiudi il calendario",
                modifier = Modifier.rotate(rotation)
            )
        }
        AnimatedVisibility(visible = calendarVisibility.value) {
            Calendar (
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