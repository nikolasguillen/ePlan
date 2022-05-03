package com.example.eplan.presentation.navigation

sealed class NestedNavGraphs(val startDestination: String, val route: String) {

    object LoginGraph: NestedNavGraphs(startDestination = Screen.Login.route, route = "loginGraph")

    object InterventionGraph: NestedNavGraphs(startDestination = Screen.InterventionList.route, route = "interventionGraph")

    object AppointmentGraph: NestedNavGraphs(startDestination = Screen.AppointmentList.route, route = "appointmentGraph")

    object AccountGraph: NestedNavGraphs(startDestination = Screen.Account.route, route = "accountGraph")
}
