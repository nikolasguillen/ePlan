package com.example.eplan.presentation.ui.interventionList

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
import com.example.eplan.presentation.ui.components.WorkActivitiesList
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.MultiFloatingActionButton
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.util.bottomNavPadding
import java.time.LocalDate

@ExperimentalMaterial3Api
@Composable
fun InterventionListScreen(
    viewModel: InterventionListViewModel,
    onNavigate: (String) -> Unit
) {

    val interventions = viewModel.interventions
    val date = viewModel.date.value
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val calendarVisibility = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.parse(date)) }
    var isExpanded by remember { mutableStateOf(false) }

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
                    isExpanded = !isExpanded
                    val route = Screen.InterventionDetails.route + "/?date=$date"
                    onNavigate(route)
                },
                onExpandClick = {
                    isExpanded = !isExpanded
                },
                onRecordClick = {
                    onNavigate(Screen.InterventionRecord.route)
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                CollapsibleCalendar(
                    calendarVisibility = calendarVisibility,
                    date = date,
                    onDayChange = { date ->
                        selectedDate.value = LocalDate.parse(date)
                        viewModel.onTriggerEvent(InterventionListEvent.DayChangeEvent(date = date))
                        calendarVisibility.value = false
                    }
                )
                WorkActivitiesList(
                    workActivities = interventions,
                    onNavigateToActivityDetailScreen = onNavigate,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        viewModel.onTriggerEvent(InterventionListEvent.DayChangeEvent(date = date))
                    }
                )
            }

            // Ombra per quando apro il multi action FAB
            AnimatedVisibility(visible = isExpanded, enter = fadeIn(), exit = fadeOut()) {
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