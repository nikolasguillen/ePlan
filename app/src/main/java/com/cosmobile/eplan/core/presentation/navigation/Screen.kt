package com.cosmobile.eplan.core.presentation.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen(route = ScreenRoutes.AUTH)
    data object InterventionList : Screen(route = ScreenRoutes.INTERVENTION_LIST)
    data object InterventionRecord : Screen(route = ScreenRoutes.INTERVENTION_RECORD)
    data object InterventionDetails : Screen(route = ScreenRoutes.INTERVENTION_DETAILS)
    data object AppointmentList : Screen(route = ScreenRoutes.APPOINTMENT_LIST)
    data object AppointmentDetails : Screen(route = ScreenRoutes.APPOINTMENT_DETAILS)
    data object Account : Screen(route = ScreenRoutes.ACCOUNT)
    data object Settings : Screen(route = ScreenRoutes.SETTINGS)
    data object VacationRequest : Screen(route = ScreenRoutes.VACATION_REQUEST)
    data object TimeStats : Screen(route = ScreenRoutes.TIME_STATS)
}