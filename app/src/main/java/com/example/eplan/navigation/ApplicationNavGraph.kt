package com.example.eplan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eplan.model.Appointment
import com.example.eplan.model.WorkActivity
import com.example.eplan.model.fromJson
import com.example.eplan.ui.items.NavigationItem
import com.example.eplan.ui.screens.*

@Composable
fun ApplicationNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route
    ) {
        //Foglio ore
        composable(route = NavigationItem.Home.route) {
            val activities = mutableListOf<WorkActivity>()
            var i = 0
            while (i < 10) {
                activities.add(
                    WorkActivity(
                        12345,
                        "commessa",
                        "descrizione commessa",
                        "22-02-2022",
                        "08:00",
                        "09:00",
                        "2",
                        "180",
                        false
                    )
                )
                i++
            }
            HomeScreen(navController = navController, activities)
        }
        // Appuntamenti
        composable(route = NavigationItem.Appointments.route) {
            val peopleInput = mapOf(
                "Giampiero Allamprese" to true,
                "Emanuele Crescentini" to false,
                "Nikolas Guillen Leon" to true,
                "Giorgio Pierantoni" to false,
                "Natalia Diaz" to false,
                "Marco Zaccheroni" to false
            )
            val appointments = mutableListOf<Appointment>()
            var i = 0
            while (i < 10) {
                appointments.add(
                    Appointment(
                        "bomba atomica",
                        "appuntamento",
                        "descrizione",
                        "22-02-2022",
                        "08:00",
                        "09:00",
                        false,
                        false,
                        peopleInput,
                        Appointment.Periodicity.Bimestrale.toString(),
                        "27-02-2022",
                        true,
                        "10",
                        "minuti"
                    )
                )
                i++
            }
            AppointmentsScreen(navController = navController, appointments = appointments)
        }
        // Account
        composable(route = "account") {
            AccountScreen(navController = navController)
        }
        // Dettaglio attività
        composable(
            route = "activityDetails/{workActivity}",
            arguments = listOf(navArgument("workActivity") { type = NavType.StringType })
        ) {
            it.arguments?.getString("workActivity")?.let { jsonString ->
                val workActivity = jsonString.fromJson(WorkActivity::class.java)
                ActivityDetailsScreen(
                    workActivity = workActivity,
                    navController = navController
                )
            }
        }
        // Dettaglio appuntamento
        composable(
            route = "appointmentDetails/{appointment}",
            arguments = listOf(navArgument("appointment") { type = NavType.StringType })
        ) {
            it.arguments?.getString("appointment")?.let { jsonString ->
                val appointment = jsonString.fromJson(Appointment::class.java)
                AppointmentDetailsScreen(
                    appointment = appointment,
                    navController = navController
                )
            }
        }
    }
}