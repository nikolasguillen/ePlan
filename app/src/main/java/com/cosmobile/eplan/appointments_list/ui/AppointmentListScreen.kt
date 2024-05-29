package com.cosmobile.eplan.appointments_list.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.DayChangeEvent
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnDecreaseDate
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnIncreaseDate
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnToggleCalendarState
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnToggleListState
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnToggleVisualizationMode
import com.cosmobile.eplan.core.presentation.navigation.BottomNavBarItem
import com.cosmobile.eplan.core.presentation.navigation.NavArguments
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs
import com.cosmobile.eplan.core.presentation.navigation.Screen
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.BottomNavBar
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CollapsibleCalendar
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.TopBar
import com.cosmobile.eplan.core.presentation.ui.components.workActivity.WorkActivitiesList
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(
    state: AppointmentListUiState,
    onEvent: (AppointmentListEvent) -> Unit,
    onNavigate: (String) -> Unit
) {
    var isFabExpanded by remember { mutableStateOf(false) }
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
                title = stringResource(R.string.appuntamenti),
                onNavigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.AppointmentList.route,
                items = listOf(BottomNavBarItem.Home, BottomNavBarItem.Appointments),
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isFabExpanded = !isFabExpanded
                    val route =
                        Screen.AppointmentDetails.route + "/?${NavArguments.DATE}=${state.selectedDate}"
                    onNavigate(route)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.aggiungi_attivita)
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
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
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
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
                isListCollapsed = state.isListCollapsed,
                displayMode = state.displayMode
            )
            WorkActivitiesList(
                workActivities = state.appointments,
                onNavigateToActivityDetailScreen = onNavigate,
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(DayChangeEvent(forceRefresh = true)) },
                showAbsentConnectionScreen = state.showAbsentConnectionScreen,
                isListCollapsed = state.isListCollapsed,
                isDayCompleted = state.isDayCompleted
            )
        }
    }
}