package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.MultiFloatingActionButton
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.util.bottomNavPadding
import java.time.LocalDate

@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit
) {

    val workActivities = viewModel.workActivities
    val date = viewModel.date.value
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val calendarVisibility = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.parse(date)) }
    val isExpanded = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = viewModel.isReady) {
        if (viewModel.isReady) {
            viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
        }
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
                    val route = Screen.WorkActivityDetails.route + "/activityId=${id}?date=${date}"
                    onNavigate(route)
                },
                onExpandClick = {
                    isExpanded.value = !isExpanded.value
                },
                onRecordClick = {
                    onNavigate(Screen.WorkActivityRecord.route)
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                // Riga con data che al click apre/chiude il calendario
                CollapsibleCalendar(
                    calendarVisibility = calendarVisibility,
                    date = date,
                    onDayChange = { date ->
                        selectedDate.value = LocalDate.parse(date)
                        viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
                        calendarVisibility.value = false
                    }
                )
                ActivitiesList(
                    workActivities = workActivities.value,
                    onNavigateToActivityDetailScreen = onNavigate,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        viewModel.onTriggerEvent(
                            ActivityListEvent.DayChangeEvent(
                                date = date
                            )
                        )
                    }
                )
            }

            // Ombra per quando apro il multi action FAB
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