package com.example.eplan.presentation.ui.appointmentList

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.uiElements.TopBar
import com.example.eplan.presentation.ui.components.workActivity.WorkActivitiesList
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(
    viewModel: AppointmentListViewModel,
    onNavigate: (String) -> Unit
) {
    val appointments = viewModel.appointments
    val date = viewModel.date.value
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val calendarVisibility = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.parse(date)) }
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var backPressedTime: Long = 0
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.appuntamenti),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.AppointmentList.route,
                items = listOf(BottomNavBarItems.Home, BottomNavBarItems.Appointments),
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isExpanded = !isExpanded
                    val route = Screen.AppointmentDetails.route + "/?date=$date"
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
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            CollapsibleCalendar(
                calendarVisibility = calendarVisibility,
                date = date,
                onDayChange = { date ->
                    selectedDate.value = LocalDate.parse(date)
                    viewModel.onTriggerEvent(AppointmentListEvent.DayChangeEvent(date = date))
                    calendarVisibility.value = false
                }
            )
            WorkActivitiesList(
                workActivities = appointments,
                onNavigateToActivityDetailScreen = onNavigate,
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.onTriggerEvent(AppointmentListEvent.DayChangeEvent(date = date))
                }
            )
        }
    }
}