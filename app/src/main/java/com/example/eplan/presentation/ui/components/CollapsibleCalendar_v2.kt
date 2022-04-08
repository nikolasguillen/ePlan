package com.example.eplan.presentation.ui.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.presentation.util.fromLocalDateToDate
import java.time.LocalDate

@Composable
fun CollapsibleCalendar_v2(
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