package com.example.eplan.presentation.navigation

sealed class Screen(val route: String) {

    object WorkActivityList: Screen("workActivityList")

    object WorkActivityDetails: Screen("workActivityDetails")

    object AppointmentList: Screen("appointmentList")

    object AppointmentDetails: Screen("appointmentDetails")

    object Account: Screen("account")
}