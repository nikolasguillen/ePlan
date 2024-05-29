package com.cosmobile.eplan.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.presentation.navigation.Screen

sealed class AccountItems(val nameResId: Int, val route: String, val icon: ImageVector) {
    data object Settings : AccountItems(
        nameResId = R.string.impostazioni,
        route = Screen.Settings.route,
        icon = Icons.Filled.Settings
    )

    data object TimeStats : AccountItems(
        nameResId = R.string.statistiche_ore,
        route = Screen.TimeStats.route,
        icon = Icons.Filled.Leaderboard
    )

    data object VacationRequest : AccountItems(
        nameResId = R.string.richiedi_ferie,
        route = Screen.VacationRequest.route,
        icon = Icons.Filled.Sailing
    )
}