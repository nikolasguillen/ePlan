package com.example.eplan.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.workActivity.ActivityDetailsScreen
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivityList.ActivitiesListScreen
import com.example.eplan.presentation.ui.workActivityList.ActivityListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*companion object {
        private lateinit var instance: MainActivity

        private val database: EplanDatabase by lazy {
            EplanDatabase.buildDatabase(instance)
        }
    }*/

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.WorkActivityList.route
            ) {
                composable(route = Screen.WorkActivityList.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: ActivityListViewModel =
                        viewModel(key = "ActivityListViewModel", factory = factory)
                    ActivitiesListScreen(
                        viewModel = viewModel,
                        onNavigate = navController::navigate
                    )
                }

                composable(
                    route = Screen.WorkActivityDetails.route + "/{activityId}",
                    arguments = listOf(
                        navArgument("activityId") {
                            type = NavType.IntType
                        })
                ) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: ActivityDetailViewModel =
                        viewModel(key = "ActivityDetailsViewModel", factory = factory)
                    ActivityDetailsScreen(
                        activityId = navBackStackEntry.arguments?.getInt("activityId")!!,
                        viewModel = viewModel,
                        onSavePressed = ,
                        onBackPressed = ,
                        onDeletePressed =
                    )
                }
            }
        }
    }
}