package com.example.eplan.presentation.navigation

sealed class Screen(val route: String, val name: String) {

    object WorkActivityList: Screen(route = "workActivityList", name = "Foglio ore")

    object WorkActivityDetails: Screen(route = "workActivityDetails", name = "Attività")

    object AppointmentList: Screen(route = "appointmentList", name = "Appuntamenti")

    object AppointmentDetails: Screen(route = "appointmentDetails", name = "Appuntamento")

    object Account: Screen(route = "account", name = "Account")
}