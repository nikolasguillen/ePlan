package com.example.eplan.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eplan.presentation.ui.appointmentList.AppointmentListScreen
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.*
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityDetailsScreen
import com.example.eplan.presentation.ui.workActivityList.ActivitiesListScreen
import com.example.eplan.presentation.ui.workActivityList.ActivityListViewModel

@Composable
fun NavGraph(navController: NavHostController, bottomPadding: Dp) {
    NavHost(
        navController = navController,
        startDestination = Screen.WorkActivityList.route,
        modifier = Modifier.statusBarsPadding()
    ) {

        composable(route = Screen.WorkActivityList.route) { navBackStackEntry ->
            /*val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
            val viewModel: ActivityListViewModel =
                viewModel(key = "ActivityListViewModel", factory = factory)*/

            val viewModel = hiltViewModel<ActivityListViewModel>()
            ActivitiesListScreen(
                viewModel = viewModel,
                onNavigate = navController::navigate,
                bottomPadding = bottomPadding
            )
        }

        composable(
            route = Screen.WorkActivityDetails.route + "/{activityId}",
            arguments = listOf(
                navArgument("activityId") {
                    type = NavType.StringType
                })
        ) { navBackStackEntry ->
            /*val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
            val viewModel: ActivityDetailViewModel =
                viewModel(key = "ActivityDetailsViewModel", factory = factory)*/

            val viewModel = hiltViewModel<ActivityDetailViewModel>()
            ActivityDetailsScreen(
                activityId = navBackStackEntry.arguments?.getString("activityId")!!,
                viewModel = viewModel,
                onSavePressed = {
                    viewModel.onTriggerEvent(UpdateActivityEvent)
                    navController.navigateUp()
                },
                onBackPressed = navController::navigateUp,
                onDeletePressed = {
                    viewModel.onTriggerEvent(DeleteActivityEvent)
                    navController.navigateUp()
                }
            )
        }

        /*TODO da sistemare*/
        composable(route = Screen.AppointmentList.route) {
            AppointmentListScreen(navController = navController)
        }
    }
}