package com.example.eplan.presentation.navigation

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.eplan.presentation.navigation.NestedNavGraphs.*
import com.example.eplan.presentation.ui.account.AccountScreen
import com.example.eplan.presentation.ui.account.AccountViewModel
import com.example.eplan.presentation.ui.appointmentList.AppointmentListScreen
import com.example.eplan.presentation.ui.appointmentList.AppointmentListViewModel
import com.example.eplan.presentation.ui.camera.CameraEvent
import com.example.eplan.presentation.ui.camera.CameraScreen
import com.example.eplan.presentation.ui.camera.CameraViewModel
import com.example.eplan.presentation.ui.login.LoginEvent.LoginAttemptEvent
import com.example.eplan.presentation.ui.login.LoginScreen
import com.example.eplan.presentation.ui.login.LoginViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.DeleteActivityEvent
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityDetailsScreen
import com.example.eplan.presentation.ui.workActivity.ActivityFormEvent
import com.example.eplan.presentation.ui.workActivityList.ActivitiesListScreen
import com.example.eplan.presentation.ui.workActivityList.ActivityListViewModel
import com.example.eplan.presentation.ui.workActivityRecord.ActivityRecordScreen
import com.example.eplan.presentation.ui.workActivityRecord.ActivityRecordViewModel
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LoginGraph.route,
        modifier = Modifier.statusBarsPadding()
    ) {

        navigation(startDestination = LoginGraph.startDestination, route = LoginGraph.route) {
            composable(route = Screen.Login.route) {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginAttempted = { viewModel.onTriggerEvent(LoginAttemptEvent) }
                )
                if (viewModel.successfulLoginAttempt.value) {
                    navController.popBackStack()
                    navController.navigate(WorkActivityGraph.route)
                    viewModel.successfulLoginAttempt.value = false
                }
            }

            navigation(
                startDestination = WorkActivityGraph.startDestination,
                route = WorkActivityGraph.route
            ) {

                val selectedDate = mutableStateOf(LocalDate.now())

                composable(route = Screen.WorkActivityList.route) {
                    val viewModel = hiltViewModel<ActivityListViewModel>()
                    ActivitiesListScreen(
                        viewModel = viewModel,
                        onNavigate = navController::navigate,
                    )
                }

                composable(route = Screen.WorkActivityRecord.route) {
                    val viewModel = hiltViewModel<ActivityRecordViewModel>()
                    ActivityRecordScreen(
                        viewModel = viewModel,
                        onSave = {
                            navController.popBackStack()
                            navController.navigate(it)
                        }
                    )
                }

                composable(
                    route = Screen.WorkActivityDetails.route + "/activityId={activityId}?date={date}&start={start}&end={end}",
                    arguments = listOf(
                        navArgument("activityId") {
                            type = NavType.StringType
                        },
                        navArgument("date") {
                            type = NavType.StringType
                            defaultValue = selectedDate.value.toString()
                        },
                        navArgument("start") {
                            type = NavType.StringType
                            nullable = true
                        },
                        navArgument("end") {
                            type = NavType.StringType
                            nullable = true
                        }
                    )
                ) { navBackStackEntry ->
                    val viewModel = hiltViewModel<ActivityDetailViewModel>()
                    val date = LocalDate.parse(navBackStackEntry.arguments?.getString("date"))
                    val start = navBackStackEntry.arguments?.getString("start")
                    val end = navBackStackEntry.arguments?.getString("end")
                    ActivityDetailsScreen(
                        activityId = navBackStackEntry.arguments?.getString("activityId")!!,
                        viewModel = viewModel,
                        onSavePressed = {
                            viewModel.onFormEvent(ActivityFormEvent.Submit)

                        },
                        onBackPressed = navController::popBackStack,
                        onDeletePressed = {
                            viewModel.onTriggerEvent(DeleteActivityEvent)
                            navController.popBackStack()
                        },
                        date = date,
                        start = if (start == null) null else LocalTime.parse(start),
                        end = if (end == null) null else LocalTime.parse(end)
                    )
                }
            }

            navigation(
                startDestination = AppointmentGraph.startDestination,
                route = AppointmentGraph.route
            ) {
                /*TODO da sistemare*/
                composable(route = Screen.AppointmentList.route) {
                    val viewModel = hiltViewModel<AppointmentListViewModel>()
                    AppointmentListScreen(
                        viewModel = viewModel,
                        onNavigate = navController::navigate
                    )

                }
            }

            navigation(
                startDestination = AccountGraph.startDestination,
                route = AccountGraph.route
            ) {
                composable(route = Screen.Account.route) {
                    val viewModel = hiltViewModel<AccountViewModel>()
                    AccountScreen(
                        viewModel = viewModel,
                        onBackPressed = navController::popBackStack,
                        navigateToCamera = { navController.navigate(Screen.Camera.route) },
                        toProfile = {},
                        toAppInfo = {},
                        toSettings = {},
                        logout = {
                            navController.navigate(route = LoginGraph.startDestination)
                        }
                    )
                }
                composable(route = Screen.Camera.route) {
                    val viewModel = hiltViewModel<CameraViewModel>()
                    CameraScreen(
                        viewModel = viewModel,
                        onBackPressed = navController::popBackStack,
                        onImageSelected = {
                            viewModel.onTriggerEvent(CameraEvent.SaveUriInCache)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}