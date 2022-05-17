package com.example.eplan.presentation.ui.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.eplan.R
import com.example.eplan.presentation.navigation.Screen

sealed class AccountItems(val nameResId: Int, val route: String, val icon: ImageVector) {
    object Settings : AccountItems(nameResId = R.string.impostazioni, route = Screen.Settings.route, icon = Icons.Filled.Settings)
    object TimeStats : AccountItems(nameResId = R.string.statistiche_ore, route = "TODO", icon = Icons.Filled.Leaderboard)
    object VacationRequest: AccountItems(nameResId = R.string.richiesta_ferie, route = Screen.VacationRequest.route, icon = Icons.Filled.Sailing)
}