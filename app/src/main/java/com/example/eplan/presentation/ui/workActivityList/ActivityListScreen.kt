package com.example.eplan.presentation.ui.workActivityList

import android.graphics.Color
import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.GifImage
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.*
import com.example.eplan.presentation.util.fromLocalDateToDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


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
                        .padding(16.dp)
                ) {
                    val date = LocalDate.parse(query)
                        .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

                    Text(
                        text = date.split(" ")[0] + " " + date.split(" ")[1].replaceFirstChar { it.uppercase() } + " " + date.split(
                            " "
                        )[2],
                        style = MaterialTheme.typography.titleMedium
                    )
                    Crossfade(targetState = calendarVisibility.value) { isVisible ->
                        if (isVisible) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = "Chiudi il calendario"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Apri il calendario"
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = calendarVisibility.value) {
                    AndroidView(
                        factory = { context ->
                            CalendarView(context).apply {
                                this.date = fromLocalDateToDate(LocalDate.parse(query)).time
                                this.setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                                    this.date = fromLocalDateToDate(selectedDate).time
                                    viewModel.onQueryChanged(selectedDate.toString())
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
                if (workActivities.value.isEmpty() && !loading) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        GifImage(
                            imageID = R.drawable.travolta,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 64.dp)
                        )
                        Text(text = "Non ci sono interventi da mostrare :(", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    ActivitiesList(
                        workActivities = workActivities,
                        onNavigateToActivityDetailScreen = onNavigate,
                        loading = loading
                    )
                }
            }
        }
    )
}