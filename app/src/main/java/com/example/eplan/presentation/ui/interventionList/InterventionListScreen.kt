package com.example.eplan.presentation.ui.interventionList

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.BottomNavBarItem
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.*
import com.example.eplan.presentation.ui.components.uiElements.BottomNavBar
import com.example.eplan.presentation.ui.components.uiElements.MultiFloatingActionButton
import com.example.eplan.presentation.ui.components.uiElements.TopBar
import com.example.eplan.presentation.ui.components.workActivity.WorkActivitiesList
import kotlinx.coroutines.launch
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
    val context = LocalContext.current
    var backPressedTime: Long = 0
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.onTriggerEvent(InterventionListEvent.DayChangeEvent(date = date))
    }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.InterventionList.route,
                items = listOf(BottomNavBarItem.Home, BottomNavBarItem.Appointments),
                onNavigate = onNavigate
            )
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
                },
                isConnectionAvailable = viewModel.isConnectionAvailable
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
}