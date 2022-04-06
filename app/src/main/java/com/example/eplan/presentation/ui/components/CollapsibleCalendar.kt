package com.example.eplan.presentation.ui.components

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.time.LocalDate

@Composable
fun CollapsibleCalendar(
    startDate: LocalDate,
    onDaySelected: (dayOfMonth: Int, week: Int, year: Int) -> Unit
) {

    val todayBackgroundColor = MaterialTheme.colorScheme.primary.toArgb()
    val todayTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val selectedDayBackgroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val selectedDayTextColor = MaterialTheme.colorScheme.surface.toArgb()
    val dynamicTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            CollapsibleCalendar(context).apply {

                /* se metto lunedi' come primo giorno, si sfasa tutto */
//                this.firstDayOfWeek = 1
                this.select(Day(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth))
                this.selectedDay =
                    Day(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                textColor = dynamicTextColor
                setExpandIconColor(dynamicTextColor)

                // Material dynamic theme
                AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                    ?.setTint(selectedDayBackgroundColor)
                selectedItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                selectedItemTextColor = selectedDayTextColor
                AppCompatResources.getDrawable(context, R.drawable.today_circle)
                    ?.setTint(todayBackgroundColor)
                todayItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.today_circle)
                todayItemTextColor = todayTextColor

                setCalendarListener(object : CollapsibleCalendar.CalendarListener {
                    override fun onClickListener() {
                    }

                    override fun onDataUpdate() {
                    }

                    override fun onDayChanged() {
                    }

                    override fun onDaySelect() {
                        selectedDay?.let {
                            onDaySelected(it.day, it.month + 1, it.year)
                            collapse(100)
                        }
                    }

                    override fun onItemClick(v: View) {
                    }

                    override fun onMonthChange() {
                    }

                    override fun onWeekChange(position: Int) {
                    }

                })
            }
        },
        modifier = (Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 5.dp))
    )
}