package com.cosmobile.eplan.interventions_list.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.presentation.navigation.BottomNavBarItem.Appointments
import com.cosmobile.eplan.core.presentation.navigation.BottomNavBarItem.Home
import com.cosmobile.eplan.core.presentation.navigation.NavArguments
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs
import com.cosmobile.eplan.core.presentation.navigation.Screen
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.BottomNavBar
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CollapsibleCalendar
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.MultiFloatingActionButton
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.TopBar
import com.cosmobile.eplan.core.presentation.ui.components.workActivity.WorkActivitiesList
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.DayChangeEvent
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnDecreaseDate
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnIncreaseDate
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleCalendarState
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleListState
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleVisualizationMode
import kotlinx.coroutines.launch
import java.time.LocalDate

@ExperimentalMaterial3Api
@Composable
fun InterventionListScreen(
    state: InterventionListUiState,
    onEvent: (InterventionListEvent) -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    var backPressedTime: Long = 0
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        onEvent(DayChangeEvent(forceRefresh = true))
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.foglio_ore),
                onNavigate = {
                    onNavigate(NestedNavGraphs.AccountGraph.route)
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.InterventionList.route,
                items = listOf(Home, Appointments),
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            MultiFloatingActionButton(
                onAddClick = {
                    val route =
                        Screen.InterventionDetails.route + "/?${NavArguments.DATE}=${state.selectedDate}"
                    onNavigate(route)
                },
                onRecordClick = {
                    onNavigate(Screen.InterventionRecord.route)
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        BackHandler {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                (context as Activity).finish()
            } else {
                scope.launch {
                    snackBarHostState.showSnackbar(message = context.getString(R.string.doppio_tap_per_uscire))
                }
            }
            backPressedTime = System.currentTimeMillis()
        }
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            CollapsibleCalendar(
                isCalendarExpanded = state.isCalendarExpanded,
                date = state.selectedDate,
                onToggleCalendarState = { onEvent(OnToggleCalendarState) },
                onDayChange = { date -> onEvent(DayChangeEvent(date = date)) },
                onGoToCurrentDate = { onEvent(DayChangeEvent(date = LocalDate.now())) },
                onIncreaseDate = { onEvent(OnIncreaseDate) },
                onDecreaseDate = { onEvent(OnDecreaseDate) },
                onToggleVisualizationMode = { onEvent(OnToggleVisualizationMode) },
                onToggleListState = { onEvent(OnToggleListState) },
                displayMode = state.displayMode,
                isListCollapsed = state.isListCollapsed
            )
            WorkActivitiesList(
                workActivities = state.interventions,
                onNavigateToActivityDetailScreen = onNavigate,
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(DayChangeEvent(forceRefresh = true)) },
                onGapClick = { date, startTime, endTime ->
                    onNavigate(
                        Screen.InterventionDetails.route +
                                "/?${NavArguments.DATE}=${date}" +
                                "&${NavArguments.START}=${startTime}" +
                                "&${NavArguments.END}=${endTime}"
                    )
                },
                showAbsentConnectionScreen = state.showAbsentConnectionScreen,
                isListCollapsed = state.isListCollapsed,
                isDayCompleted = state.isDayCompleted
            )
        }
    }
}