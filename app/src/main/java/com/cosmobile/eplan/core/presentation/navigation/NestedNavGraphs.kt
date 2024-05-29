package com.cosmobile.eplan.core.presentation.navigation

sealed class NestedNavGraphs(val startDestination: String, val route: String) {

    object LoginGraph: NestedNavGraphs(startDestination = Screen.Auth.route, route = GraphsRoutes.AUTH_GRAPH)

    object InterventionGraph: NestedNavGraphs(startDestination = Screen.InterventionList.route, route = GraphsRoutes.INTERVENTION_GRAPH)

    object AppointmentGraph: NestedNavGraphs(startDestination = Screen.AppointmentList.route, route = GraphsRoutes.APPOINTMENT_GRAPH)

    object AccountGraph: NestedNavGraphs(startDestination = Screen.Account.route, route = GraphsRoutes.ACCOUNT_GRAPH)
}
