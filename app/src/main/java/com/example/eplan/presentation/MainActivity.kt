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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.presentation.navigation.NavGraph
import com.example.eplan.presentation.ui.theme.AppTheme
import com.example.eplan.presentation.util.THEME_STATE_KEY
import com.example.eplan.presentation.util.getCurrentRoute
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.color.DynamicColors
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var encryptedSharedPreferences: Preferences

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                val mode = sharedPreferences.getInt(
                    THEME_STATE_KEY,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
                AppCompatDelegate.setDefaultNightMode(mode)

                val navController = rememberNavController()
                val currentRoute = getCurrentRoute(navController = navController)
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                DynamicColors.applyToActivityIfAvailable(this)

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                currentRoute?.let {
                    with(it) {
                        when {
                            contains("list", true) -> {
                                window.navigationBarColor =
                                    SurfaceColors.SURFACE_2.getColor(this@MainActivity)
                                systemUiController.navigationBarDarkContentEnabled =
                                    !isSystemInDarkTheme()
                            }

                            contains("details", true) -> {
                                window.navigationBarColor =
                                    MaterialTheme.colorScheme.primary.toArgb()
                                systemUiController.navigationBarDarkContentEnabled =
                                    isSystemInDarkTheme()
                            }

                            contains("vacationRequest", true) -> {
                                window.navigationBarColor =
                                    MaterialTheme.colorScheme.primary.toArgb()
                                systemUiController.navigationBarDarkContentEnabled =
                                    isSystemInDarkTheme()
                            }

                            else -> {
                                window.navigationBarColor =
                                    MaterialTheme.colorScheme.surface.toArgb()
                                systemUiController.navigationBarDarkContentEnabled =
                                    !isSystemInDarkTheme()
                            }
                        }
                    }
                }
                NavGraph(navController = navController, shouldShowLogin = encryptedSharedPreferences.loadShouldShowLogin())
            }
        }
    }
}