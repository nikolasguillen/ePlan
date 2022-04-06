package com.example.eplan.presentation.ui.workActivityList

import android.graphics.Color
import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit
) {

    val workActivities = viewModel.workActivities

    val query = viewModel.query.value

    val loading = viewModel.loading.value

    val calendarVisibility = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.aggiungi_attivita)
                )
            }
        },
        content = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { calendarVisibility.value = !calendarVisibility.value }
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = LocalDate.parse(query)
                            .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Crossfade(targetState = calendarVisibility.value) { isVisible ->
                        if (isVisible) {
                            Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "Chiudi il calendario")
                        } else {
                            Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "Apri il calendario")
                        }
                    }
                }
                AnimatedVisibility(visible = calendarVisibility.value) {
                    AndroidView(
                        factory = { context ->
                            CalendarView(context).apply {
                                this.setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    viewModel.onQueryChanged(
                                        LocalDate.of(
                                            year,
                                            month + 1,
                                            dayOfMonth
                                        ).toString()
                                    )
                                    viewModel.onTriggerEvent(DayChangeEvent)
                                    calendarVisibility.value = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
//                CollapsibleCalendar(
//                    onDaySelected = { dayOfMonth, month, year ->
//
//                        viewModel.onQueryChanged(
//                            LocalDate.of(year, month, dayOfMonth).toString()
//                        )
//                        viewModel.onTriggerEvent(
//                            DayChangeEvent
//                        )
//                    },
//                    startDate = LocalDate.parse(query, DateTimeFormatter.ISO_DATE)
//                )
                ActivitiesList(
                    workActivities = workActivities,
                    onNavigateToActivityDetailScreen = onNavigate,
                    loading = loading
                )
            }
        }
    )
}