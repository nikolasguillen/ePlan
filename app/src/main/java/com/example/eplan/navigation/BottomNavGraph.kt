package com.example.eplan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eplan.AccountComposer
import com.example.eplan.ActivityDetails
import com.example.eplan.AppointmentsComposer
import com.example.eplan.HomeComposer
import com.example.eplan.ui.NavigationItem

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route
    ) {
        composable(route = NavigationItem.Home.route) {
            HomeComposer(navController = navController)
        }
        composable(route = NavigationItem.Appointments.route) {
            AppointmentsComposer(navController = navController)
        }
        composable(route = NavigationItem.Account.route) {
            AccountComposer(navController = navController)
        }
        composable("activityDetails/{activityName}/{activityDesc}/{date}/{start}/{end}/{oreSpostamento}/{km}/{close}") {

            ActivityDetails(
                activityName = it.arguments?.getString("activityName")!!,
                activityDescription = it.arguments?.getString("activityDesc")!!,
                date = it.arguments?.getString("date")!!,
                start = it.arguments?.getString("start")!!,
                end = it.arguments?.getString("end")!!,
                oreSpostamento = it.arguments?.getString("oreSpostamento")!!,
                km = it.arguments?.getString("km")!!,
                close = it.arguments?.getBoolean("close")!!,
                navController = navController
            )
        }
    }
}