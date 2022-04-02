package com.example.eplan.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eplan.presentation.navigation.NavGraph
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.theme.AppTheme
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

        setContent {

            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            val bottomBarState = rememberSaveable { mutableStateOf(false) }

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

                        when (currentRoute(navController = navController)) {
                            (Screen.WorkActivityList.route) -> {
                                bottomBarState.value = true
                            }
                            (Screen.AppointmentList.route) -> {
                                bottomBarState.value = true
                            }
                            else -> {
                                bottomBarState.value = false
                            }
                        }
                        AnimatedVisibility(
                            visible = bottomBarState.value,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) {
                    NavGraph(navController = navController, bottomPadding = it.calculateBottomPadding())
                }
            }
        }
    }

    @Composable
    private fun currentRoute(navController: NavHostController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }
}