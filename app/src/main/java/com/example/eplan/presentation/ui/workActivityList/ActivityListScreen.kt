package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.*
import com.example.eplan.presentation.util.bottomNavPadding
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit,
) {

    val workActivities = viewModel.workActivities

    val date = viewModel.date.value

    val loading = viewModel.loading.value

    val calendarVisibility = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(LocalDate.parse(date)) }

    val isExpanded = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(date) {
        viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
    }


    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavPadding),
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        floatingActionButton = {
            MultiFloatingActionButton(
                onAddClick = {
                    isExpanded.value = !isExpanded.value
                    val id = "null"
                    val route = Screen.WorkActivityDetails.route + "/${id}"
                    onNavigate(route)
                },
                onExpandClick = {
                    isExpanded.value = !isExpanded.value
                },
                onRecordClick = {
                    onNavigate(Screen.WorkActivityTimer.route)
                }
            )
        },
        content = {
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
                    CollapsibleCalendar(
                        date = date,
                        onDayChange = { date ->
                            selectedDate.value = LocalDate.parse(date)
                            viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
                            calendarVisibility.value = false
                        }
                    )
                }
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
                                text = "Non ci sono interventi da mostrare",
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
            AnimatedVisibility(visible = isExpanded.value, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5F))
                        .clickable(enabled = false, onClick = {})
                        .fillMaxSize()
                )
            }
        }
    )
}