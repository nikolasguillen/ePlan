package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.ui.components.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit
) {

    val workActivities = viewModel.workActivities

    val date = viewModel.date.value

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
                    val headerDate = LocalDate.parse(date)
                        .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

                    Text(
                        text = headerDate.split(" ")[0] + " " + headerDate.split(" ")[1].replaceFirstChar { it.uppercase() } + " " + headerDate.split(
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
                    CollapsibleCalendar_v2(
                        date = date,
                        onDayChange = { date ->
                            viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
                            calendarVisibility.value = false
                        }
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

                if (loading) {
                    repeat(3) {
                        PlaceholderCard()
                    }
                } else {
                    if (workActivities.value.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GifImage(
                                imageID = R.drawable.travolta,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 64.dp)
                            )
                            Text(
                                text = "Non ci sono interventi da mostrare :(",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        ActivitiesList(
                            workActivities = workActivities.value,
                            onNavigateToActivityDetailScreen = onNavigate
                        )
                    }
                }
            }
        }
    )
}