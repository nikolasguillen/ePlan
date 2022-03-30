package com.example.eplan.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.appointmentList.AppointmentListScreen
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityDetailsScreen
import com.example.eplan.presentation.ui.workActivityList.ActivitiesListScreen
import com.example.eplan.presentation.ui.workActivityList.ActivityListViewModel
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*companion object {
        private lateinit var instance: MainActivity

        private val database: EplanDatabase by lazy {
            EplanDatabase.buildDatabase(instance)
        }
    }*/

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    val cr = currentRoute(navController = navController)
                    if (cr?.contains(Screen.WorkActivityList.route) == true || cr?.contains(Screen.AppointmentList.route) == true) {
                        BottomNavBar(navController = navController)
                    }
                }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.WorkActivityList.route,
                    modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                ) {

                    composable(route = Screen.WorkActivityList.route) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val viewModel: ActivityListViewModel =
                            viewModel(key = "ActivityListViewModel", factory = factory)
                        Log.d(TAG, viewModel.toString())
                        ActivitiesListScreen(
                            viewModel = viewModel,
                            onNavigate = navController::navigate
                        )
                    }

                    composable(
                        route = Screen.WorkActivityDetails.route + "/{activityId}",
                        arguments = listOf(
                            navArgument("activityId") {
                                type = NavType.StringType
                            })
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val viewModel: ActivityDetailViewModel =
                            viewModel(key = "ActivityDetailsViewModel", factory = factory)
                        Log.d(TAG, "Bestemmio")
                        ActivityDetailsScreen(
                            activityId = navBackStackEntry.arguments?.getString("activityId")!!,
                            viewModel = viewModel,
                            onSavePressed = { workActivity ->
                                viewModel.onTriggerEvent(
                                    ActivityDetailEvent.UpdateActivityEvent(
                                        workActivity = workActivity
                                    )
                                )
                            },
                            onBackPressed = navController::navigateUp,
                            onDeletePressed = { id ->
                                viewModel.onTriggerEvent(ActivityDetailEvent.DeleteActivityEvent(id = id))
                            }
                        )
                    }

                    /*TODO da sistemare*/
                    composable(route = Screen.AppointmentList.route) {
                        AppointmentListScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Composable
    fun currentRoute(navController: NavHostController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }
}