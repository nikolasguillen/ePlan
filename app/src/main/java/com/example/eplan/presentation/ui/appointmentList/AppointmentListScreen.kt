package com.example.eplan.presentation.ui.appointmentList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.util.bottomNavPadding
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

    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavPadding),
        topBar = {
            TopBar(
                title = stringResource(R.string.appuntamenti),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
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
        content = {
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

            }
        }
    )
}