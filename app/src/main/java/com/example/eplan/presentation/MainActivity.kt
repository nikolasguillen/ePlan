package com.example.eplan.presentation

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.navigation.NavGraph
import com.example.eplan.presentation.ui.components.BottomNavBar
import com.example.eplan.presentation.ui.theme.AppTheme
import com.example.eplan.presentation.util.THEME_STATE_KEY
import com.example.eplan.presentation.util.getCurrentRoute
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val mode = sharedPreferences.getInt(THEME_STATE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(mode)

            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            var bottomBarState by rememberSaveable { mutableStateOf(false) }

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
                    modifier = Modifier.navigationBarsPadding(),
                    bottomBar = {

                        val items = listOf(
                            BottomNavBarItems.Home,
                            BottomNavBarItems.Appointments
                        )

                        bottomBarState = when (getCurrentRoute(navController = navController)) {
                            (BottomNavBarItems.Home.route) -> {
                                true
                            }
                            (BottomNavBarItems.Appointments.route) -> {
                                true
                            }
                            else -> {
                                false
                            }
                        }

                        AnimatedVisibility(
                            visible = bottomBarState,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            if (bottomBarState) {
                                BottomNavBar(navController = navController, items = items)
                            }
                        }
                    }
                ) {
                    NavGraph(navController = navController)
                }
            }
        }
    }
}