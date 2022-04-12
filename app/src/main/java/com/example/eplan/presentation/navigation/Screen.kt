package com.example.eplan.presentation.navigation

sealed class Screen(val route: String, val name: String) {

    object Login: Screen(route = "login", name = "Login")

    object WorkActivityList: Screen(route = "workActivityList", name = "Foglio ore")

    object WorkActivityTimer: Screen(route = "workActivityTimer", name = "Timer")

    object WorkActivityDetails: Screen(route = "workActivityDetails", name = "Attività")

    object AppointmentList: Screen(route = "appointmentList", name = "Appuntamenti")

    object AppointmentDetails: Screen(route = "appointmentDetails", name = "Appuntamento")

    object Account: Screen(route = "account", name = "Account")
}