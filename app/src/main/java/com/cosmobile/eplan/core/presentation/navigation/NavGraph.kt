package com.cosmobile.eplan.core.presentation.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cosmobile.eplan.R
import com.cosmobile.eplan.account.AccountEvent.Logout
import com.cosmobile.eplan.account.AccountScreen
import com.cosmobile.eplan.account.AccountViewModel
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent.DeleteAppointmentEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailScreen
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailViewModel
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnSubmit
import com.cosmobile.eplan.appointments_list.ui.AppointmentListScreen
import com.cosmobile.eplan.appointments_list.ui.AppointmentListViewModel
import com.cosmobile.eplan.auth.ui.AuthScreen
import com.cosmobile.eplan.auth.ui.LoginViewModel
import com.cosmobile.eplan.core.presentation.navigation.NavArguments.APPOINTMENT_ID
import com.cosmobile.eplan.core.presentation.navigation.NavArguments.DATE
import com.cosmobile.eplan.core.presentation.navigation.NavArguments.END
import com.cosmobile.eplan.core.presentation.navigation.NavArguments.INTERVENTION_ID
import com.cosmobile.eplan.core.presentation.navigation.NavArguments.START
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs.AccountGraph
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs.AppointmentGraph
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs.InterventionGraph
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs.LoginGraph
import com.cosmobile.eplan.core.util.UiText
import com.cosmobile.eplan.core.util.popupToLogin
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent.DeleteInterventionEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailViewModel
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailsScreen
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent
import com.cosmobile.eplan.intervention_detail.ui.record.InterventionRecordScreen
import com.cosmobile.eplan.intervention_detail.ui.record.InterventionRecordViewModel
import com.cosmobile.eplan.interventions_list.ui.InterventionListScreen
import com.cosmobile.eplan.interventions_list.ui.InterventionListViewModel
import com.cosmobile.eplan.settings.ui.SettingsScreen
import com.cosmobile.eplan.settings.ui.SettingsViewModel
import com.cosmobile.eplan.time_stats.ui.TimeStatsScreen
import com.cosmobile.eplan.time_stats.ui.TimeStatsViewModel
import com.cosmobile.eplan.vacation_request.ui.VacationRequestEvent.RequestEvent
import com.cosmobile.eplan.vacation_request.ui.VacationRequestScreen
import com.cosmobile.eplan.vacation_request.ui.VacationRequestViewModel
import java.time.Duration
import java.time.LocalDate

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController, onFinish: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = LoginGraph.route,
        modifier = Modifier.safeDrawingPadding()
    ) {
        navigation(
            startDestination = LoginGraph.startDestination,
            route = LoginGraph.route
        ) {
            composable(route = Screen.Auth.route) {
                val viewModel = hiltViewModel<LoginViewModel>()

                LaunchedEffect(key1 = Unit) {
                    viewModel.successfulLoginChannel.collect {
                        navController.popBackStack()
                        navController.navigate(InterventionGraph.route)
                    }
                }
                AuthScreen(
                    state = viewModel.state,
                    onEvent = viewModel::onEvent,
                    onFinish = onFinish
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

                LaunchedEffect(key1 = Unit) {
                    viewModel.logoutChannel.collect {
                        navController.popupToLogin()
                    }
                }

                InterventionListScreen(
                    state = viewModel.state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navController::navigate
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
                route = Screen.InterventionDetails.route +
                        "/?$INTERVENTION_ID={$INTERVENTION_ID}" +
                        "&$DATE={$DATE}" +
                        "&$START={$START}" +
                        "&$END={$END}",
                arguments = listOf(
                    navArgument(INTERVENTION_ID) {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument(DATE) {
                        type = NavType.StringType
                        defaultValue = selectedDate.value.toString()
                    },
                    navArgument(START) {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument(END) {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) {
                val viewModel = hiltViewModel<InterventionDetailViewModel>()
                val state = viewModel.state

                InterventionDetailsScreen(
                    state = state,
                    onFormEvent = viewModel::onFormEvent,
                    onEvent = viewModel::onEvent,
                    topBarTitle = UiText.StringResource(R.string.intervento),
                    onSaveAndClosePressed = {
                        viewModel.onFormEvent(InterventionFormEvent.OnSubmit { navController.popBackStack() })
                    },
                    onSaveAndContinuePressed = {
                        viewModel.onFormEvent(
                            InterventionFormEvent.OnSubmit {
                                val newInterventionStartTime = state.workActivity?.end
                                val date = state.workActivity?.date ?: return@OnSubmit

                                navController.popBackStack()
                                navController.navigate(
                                    Screen.InterventionDetails.route +
                                            "/?$DATE=${date}" +
                                            "&$START=${newInterventionStartTime}" +
                                            "&$END=${
                                                newInterventionStartTime?.plus(
                                                    Duration.ofMinutes(
                                                        15L
                                                    )
                                                )
                                            }"
                                )
                            }
                        )
                    },
                    onBackPressed = navController::popBackStack,
                    onDeletePressed = { viewModel.onEvent(DeleteInterventionEvent) },
                    validationEvents = viewModel.validationEvents,
                    retrieving = viewModel.retrieving,
                    sending = viewModel.sending
                )
            }
        }

        /* Sezione appuntamenti */
        navigation(
            startDestination = AppointmentGraph.startDestination,
            route = AppointmentGraph.route
        ) {
            composable(route = Screen.AppointmentList.route) {
                val viewModel = hiltViewModel<AppointmentListViewModel>()

                LaunchedEffect(key1 = Unit) {
                    viewModel.logoutChannel.collect {
                        navController.popupToLogin()
                    }
                }

                AppointmentListScreen(
                    state = viewModel.state,
                    onEvent = viewModel::onEvent,
                    onNavigate = navController::navigate
                )
            }

            composable(
                route = Screen.AppointmentDetails.route +
                        "/?$APPOINTMENT_ID={$APPOINTMENT_ID}" +
                        "&$DATE={$DATE}",
                arguments = listOf(
                    navArgument(APPOINTMENT_ID) {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument(DATE) {
                        type = NavType.StringType
                        defaultValue = LocalDate.now().toString()
                    }
                )
            ) {
                val viewModel = hiltViewModel<AppointmentDetailViewModel>()

                AppointmentDetailScreen(
                    state = viewModel.state,
                    onFormEvent = viewModel::onFormEvent,
                    onEvent = viewModel::onEvent,
                    topBarTitle = UiText.StringResource(R.string.appuntamento),
                    onSavePressed = {
                        viewModel.onFormEvent(OnSubmit { navController.popBackStack() })
                    },
                    onBackPressed = navController::popBackStack,
                    onDeletePressed = { viewModel.onEvent(DeleteAppointmentEvent) },
                    validationEvents = viewModel.validationEvents,
                    retrieving = viewModel.retrieving,
                    sending = viewModel.sending
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
                    onListItemClick = { navController.navigate(it) },
                    logout = {
                        viewModel.onTriggerEvent(
                            Logout {
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
                )
            }
            composable(route = Screen.Settings.route) {
                val viewModel = hiltViewModel<SettingsViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                SettingsScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onBackPressed = navController::popBackStack
                )
            }
            composable(route = Screen.TimeStats.route) {
                val viewModel = hiltViewModel<TimeStatsViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                TimeStatsScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    validationEventsFlow = viewModel.validationEvents,
                    onBackPressed = navController::popBackStack
                )
            }
            composable(route = Screen.VacationRequest.route) {
                val viewModel = hiltViewModel<VacationRequestViewModel>()
                VacationRequestScreen(
                    viewModel = viewModel,
                    onRequestSent = { startDateMillis, endDateMillis ->
                        viewModel.onTriggerEvent(RequestEvent(startDateMillis, endDateMillis))
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