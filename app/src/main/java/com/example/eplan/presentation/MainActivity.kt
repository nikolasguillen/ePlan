package com.example.eplan.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eplan.presentation.navigation.NavGraph
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.appointmentList.AppointmentListScreen
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.theme.AppTheme
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityDetailsScreen
import com.example.eplan.presentation.ui.workActivityList.ActivitiesListScreen
import com.example.eplan.presentation.ui.workActivityList.ActivityListViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*companion object {
        private lateinit var instance: MainActivity

        private val database: EplanDatabase by lazy {
            EplanDatabase.buildDatabase(instance)
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent() {

            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            AppTheme {

                Scaffold(
                    bottomBar = {
                        val cr = currentRoute(navController = navController)
                        if (cr?.contains(Screen.WorkActivityList.route) == true || cr?.contains(
                                Screen.AppointmentList.route
                            ) == true
                        ) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) {
                    NavGraph(navController = navController)
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