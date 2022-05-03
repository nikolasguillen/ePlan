package com.example.eplan.presentation.navigation

sealed class Screen(val route: String, val name: String) {

    object Login: Screen(route = "login", name = "Login")

    object InterventionList: Screen(route = "interventionList", name = "Foglio ore")

    object InterventionRecord: Screen(route = "interventionTimer", name = "Timer")

    object InterventionDetails: Screen(route = "interventionDetails", name = "Attività")

    object AppointmentList: Screen(route = "appointmentList", name = "Appuntamenti")

    object AppointmentDetails: Screen(route = "appointmentDetails", name = "Appuntamento")

    object Account: Screen(route = "account", name = "Account")

    object Camera: Screen(route = "camera", name = "Camera")
}