package com.example.eplan.presentation.ui.composables

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = if (dynamic) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context = context) else dynamicLightColorScheme(
            context = context
        )
    } else {
        if (darkTheme) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        content = content,
        colorScheme = colorScheme
    )
}