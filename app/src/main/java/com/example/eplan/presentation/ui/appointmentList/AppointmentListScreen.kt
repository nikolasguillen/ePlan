package com.example.eplan.presentation.ui.appointmentList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.domain.model.Appointment
import com.example.eplan.presentation.ui.components.AppointmentCard
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(navController: NavHostController, appointments: MutableList<Appointment>) {

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = { TopBar(stringResource(R.string.appuntamenti), navigate = { navController.navigate("account") } ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
        },
        content = {
            Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                CollapsibleCalendar()
                LazyColumn {
                    items(appointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}