package com.example.eplan.presentation.navigation

sealed class Screen(val route: String, val name: String) {

    object Login: Screen(route = "login", name = "Login")

    object InterventionList: Screen(route = "interventionList", name = "Interventions list")

    object InterventionRecord: Screen(route = "interventionTimer", name = "Timer")

    object InterventionDetails: Screen(route = "interventionDetails", name = "Intervention detail")

    object AppointmentList: Screen(route = "appointmentList", name = "Appointments list")

    object AppointmentDetails: Screen(route = "appointmentDetails", name = "Appointment")

    object Account: Screen(route = "account", name = "Account")

    object Camera: Screen(route = "camera", name = "Camera")

    object Settings: Screen(route = "settings", name = "Settings")

    object VacationRequest: Screen(route = "vacationRequest", name = "Vacation request")

    object TimeStats: Screen(route = "timeStats", name = "Time stats")
}