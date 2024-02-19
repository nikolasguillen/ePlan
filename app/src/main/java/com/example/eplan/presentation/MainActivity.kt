package com.example.eplan.presentation

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.interactors.login.RefreshToken
import com.example.eplan.presentation.navigation.NavGraph
import com.example.eplan.presentation.ui.theme.AppTheme
import com.example.eplan.presentation.util.STAY_LOGGED
import com.example.eplan.presentation.util.THEME_STATE_KEY
import com.example.eplan.presentation.util.getCurrentRoute
import com.google.android.material.color.DynamicColors
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var encryptedSharedPreferences: Preferences

    @Inject
    lateinit var refreshToken: RefreshToken

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        this.enableEdgeToEdge()

        setContent {

            LaunchedEffect(key1 = Unit) {
                refreshToken.execute().onEach { dataState ->
                    dataState.data?.let {
                        if (it.first.toString().startsWith("2")) {
                            encryptedSharedPreferences.saveToken(token = it.second)
                        } else {
                            encryptedSharedPreferences.deleteToken()
                            encryptedSharedPreferences.deleteUsername()
                            encryptedSharedPreferences.deleteProfilePicUri()
                            sharedPreferences.edit().putBoolean(STAY_LOGGED, false).apply()
                        }
                    }
                }
            }

            AppTheme {

                val mode = sharedPreferences.getInt(
                    THEME_STATE_KEY,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
                AppCompatDelegate.setDefaultNightMode(mode)

                val navController = rememberNavController()
                val currentRoute = getCurrentRoute(navController = navController)
                val useDarkIcons = !isSystemInDarkTheme()
                DynamicColors.applyToActivityIfAvailable(this)

                currentRoute?.let {
                    with(it) {
                        when {
                            contains("list", true) -> {
                                window.navigationBarColor =
                                    SurfaceColors.SURFACE_2.getColor(this@MainActivity)
                                window.statusBarColor = colorScheme.background.toArgb()
                                WindowCompat.getInsetsController(
                                    window,
                                    LocalView.current
                                ).isAppearanceLightStatusBars = useDarkIcons
                            }

                            contains("details", true) -> {
                                window.navigationBarColor = colorScheme.primary.toArgb()
                                window.statusBarColor = colorScheme.background.toArgb()
                            }

                            contains("vacationRequest", true) -> {
                                window.navigationBarColor = colorScheme.primary.toArgb()
                                window.statusBarColor = colorScheme.background.toArgb()
                            }

                            else -> {
                                window.navigationBarColor = colorScheme.surface.toArgb()
                            }
                        }
                    }
                }

                NavGraph(
                    navController = navController,
                    shouldShowLogin = !sharedPreferences.getBoolean(
                        STAY_LOGGED, false
                    )
                )
            }
        }
    }
}