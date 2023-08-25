package com.example.eplan.presentation.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs.AccountGraph
import com.example.eplan.presentation.navigation.NestedNavGraphs.AppointmentGraph
import com.example.eplan.presentation.navigation.NestedNavGraphs.InterventionGraph
import com.example.eplan.presentation.navigation.NestedNavGraphs.LoginGraph
import com.example.eplan.presentation.ui.account.AccountEvent
import com.example.eplan.presentation.ui.account.AccountScreen
import com.example.eplan.presentation.ui.account.AccountViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailEvent
import com.example.eplan.presentation.ui.appointment.AppointmentDetailScreen
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent
import com.example.eplan.presentation.ui.appointmentList.AppointmentListScreen
import com.example.eplan.presentation.ui.appointmentList.AppointmentListViewModel
import com.example.eplan.presentation.ui.camera.CameraEvent
import com.example.eplan.presentation.ui.camera.CameraScreen
import com.example.eplan.presentation.ui.camera.CameraViewModel
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.DeleteInterventionEvent
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionDetailsScreen
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent
import com.example.eplan.presentation.ui.interventionList.InterventionListScreen
import com.example.eplan.presentation.ui.interventionList.InterventionListViewModel
import com.example.eplan.presentation.ui.interventionRecord.InterventionRecordScreen
import com.example.eplan.presentation.ui.interventionRecord.InterventionRecordViewModel
import com.example.eplan.presentation.ui.login.LoginEvent.LoginAttemptEvent
import com.example.eplan.presentation.ui.login.LoginScreen
import com.example.eplan.presentation.ui.login.LoginViewModel
import com.example.eplan.presentation.ui.settings.SettingsScreen
import com.example.eplan.presentation.ui.settings.SettingsViewModel
import com.example.eplan.presentation.ui.timeStats.TimeStatsScreen
import com.example.eplan.presentation.ui.timeStats.TimeStatsViewModel
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestEvent.RequestEvent
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestScreen
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestViewModel
import java.time.Duration
import java.time.LocalDate

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController, shouldShowLogin: Boolean) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (shouldShowLogin) LoginGraph.route else InterventionGraph.route,
        modifier = Modifier.systemBarsPadding()
    ) {

        navigation(
            startDestination = LoginGraph.startDestination,
            route = LoginGraph.route
        ) {
            composable(route = Screen.Login.route) {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginAttempted = { viewModel.onTriggerEvent(LoginAttemptEvent) },
                    onSuccessfulLogin = {
                        navController.popBackStack()
                        navController.navigate(InterventionGraph.route)
                    }
                )
            }
        }

        navigation(
            startDestination = InterventionGraph.startDestination,
            route = InterventionGraph.route
        ) {

            val selectedDate = mutableStateOf(LocalDate.now())

            composable(route = Screen.InterventionList.route) {
                val viewModel = hiltViewModel<InterventionListViewModel>()
                InterventionListScreen(
                    viewModel = viewModel,
                    onNavigate = navController::navigate,
                )
            }

            composable(route = Screen.InterventionRecord.route) {
                val viewModel = hiltViewModel<InterventionRecordViewModel>()
                InterventionRecordScreen(
                    viewModel = viewModel,
                    onSave = {
                        navController.popBackStack()
                        navController.navigate(it)
                    }
                )
            }

            composable(
                route = Screen.InterventionDetails.route + "/?activityId={activityId}&date={date}&start={start}&end={end}",
                arguments = listOf(
                    navArgument("activityId") {
                        type = NavType.StringType
                        nullable = true
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
            ) {
                val viewModel = hiltViewModel<InterventionDetailViewModel>()
                InterventionDetailsScreen(
                    viewModel = viewModel,
                    topBarTitleResID = R.string.intervento,
                    onSaveAndClosePressed = {
                        viewModel.onFormEvent(InterventionFormEvent.Submit)
                    },
                    onSaveAndContinuePressed = {
                        viewModel.onFormEvent(InterventionFormEvent.Submit)

                        if(viewModel.isConnectionAvailable(context = context)) {
                            val newInterventionStartTime = viewModel.intervention.value?.end
                            navController.popBackStack()
                            navController.navigate(
                                "${Screen.InterventionDetails.route}/?date=${selectedDate.value}&start=${newInterventionStartTime}&end=${
                                    newInterventionStartTime?.plus(
                                        Duration.ofMinutes(15L)
                                    )
                                }"
                            )
                        }
                    },
                    onBackPressed = navController::popBackStack,
                    onDeletePressed = {
                        viewModel.onTriggerEvent(DeleteInterventionEvent)
                        navController.popBackStack()
                    }
                )
            }
        }

        /* Sezione appuntamenti */
        navigation(
            startDestination = AppointmentGraph.startDestination,
            route = AppointmentGraph.route
        ) {

            val selectedDate = mutableStateOf(LocalDate.now())

            composable(route = Screen.AppointmentList.route) {
                val viewModel = hiltViewModel<AppointmentListViewModel>()
                AppointmentListScreen(
                    viewModel = viewModel,
                    onNavigate = navController::navigate
                )
            }
            composable(
                route = Screen.AppointmentDetails.route + "/?appointmentId={appointmentId}&date={date}",
                arguments = listOf(
                    navArgument("appointmentId") {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument("date") {
                        type = NavType.StringType
                        defaultValue = selectedDate.value.toString()
                    }
                )
            ) {
                val viewModel = hiltViewModel<AppointmentDetailViewModel>()
                AppointmentDetailScreen(
                    viewModel = viewModel,
                    topBarTitleResID = R.string.appuntamento,
                    onSavePressed = {
                        viewModel.onFormEvent(AppointmentFormEvent.Submit)
                    },
                    onBackPressed = navController::popBackStack,
                    onDeletePressed = {
                        viewModel.onTriggerEvent(AppointmentDetailEvent.DeleteAppointmentEvent)
                        navController.popBackStack()
                    }
                )
            }
        }

        /* Sezione account */
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
                    onListItemClick = { navController.navigate(it) },
                    logout = {
                        viewModel.onTriggerEvent(AccountEvent.Logout)
                        navController.popBackStack(
                            route = AccountGraph.startDestination,
                            inclusive = true
                        )
                        navController.popBackStack(
                            route = AppointmentGraph.startDestination,
                            inclusive = true
                        )
                        navController.popBackStack(
                            route = InterventionGraph.startDestination,
                            inclusive = true
                        )
                        navController.popBackStack(
                            route = LoginGraph.startDestination,
                            inclusive = true
                        )
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
            composable(route = Screen.Settings.route) {
                val viewModel = hiltViewModel<SettingsViewModel>()
                SettingsScreen(
                    viewModel = viewModel,
                    onBackPressed = navController::popBackStack
                )
            }
            composable(route = Screen.TimeStats.route) {
                val viewModel = hiltViewModel<TimeStatsViewModel>()
                TimeStatsScreen(
                    viewModel = viewModel,
                    onBackPressed = navController::popBackStack
                )
            }
            composable(route = Screen.VacationRequest.route) {
                val viewModel = hiltViewModel<VacationRequestViewModel>()
                VacationRequestScreen(
                    viewModel = viewModel,
                    onRequestSent = {
                        viewModel.onTriggerEvent(RequestEvent)
                        if (viewModel.successfulVacationRequest.value == true) {
                            navController.popBackStack()
                        }
                    },
                    onBackPressed = navController::popBackStack
                )
            }
        }
    }
}