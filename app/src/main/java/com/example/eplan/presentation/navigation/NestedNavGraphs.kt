package com.example.eplan.presentation.navigation

sealed class NestedNavGraphs(val startDestination: String, val route: String) {

    object LoginGraph: NestedNavGraphs(startDestination = Screen.Login.route, route = "loginGraph")

    object WorkActivityGraph: NestedNavGraphs(startDestination = Screen.WorkActivityList.route, route = "workActivityGraph")

    object AppointmentGraph: NestedNavGraphs(startDestination = Screen.AppointmentList.route, route = "appointmentGraph")

    object AccountGraph: NestedNavGraphs(startDestination = Screen.Account.route, route = "accountGraph")
}
