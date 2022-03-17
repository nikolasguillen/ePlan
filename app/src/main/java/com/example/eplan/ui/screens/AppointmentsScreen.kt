package com.example.eplan.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.model.Appointment
import com.example.eplan.model.WorkActivity
import com.example.eplan.ui.items.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(navController: NavHostController, appointments: MutableList<Appointment>) {

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = { TopBar(stringResource(R.string.appuntamenti), navController = navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
        },
        content = {
            Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                SetupCalendar()
                LazyColumn() {
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