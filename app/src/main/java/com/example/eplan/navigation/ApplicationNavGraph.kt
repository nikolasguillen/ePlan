package com.example.eplan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eplan.ActivityDetails
import com.example.eplan.HomeComposer

@Composable
fun ApplicationNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeComposer(navController = navController)
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