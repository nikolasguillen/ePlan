package com.example.eplan.presentation.ui.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.eplan.R

sealed class AccountItems(val nameResId: Int, val icon: ImageVector) {
    object Settings : AccountItems(nameResId = R.string.impostazioni, icon = Icons.Filled.Settings)
    object TimeStats : AccountItems(nameResId = R.string.statistiche_ore, icon = Icons.Filled.Leaderboard)
    object VacationRequest: AccountItems(nameResId = R.string.richiesta_ferie, icon = Icons.Filled.Sailing)
    object Logout : AccountItems(nameResId = R.string.logout, icon = Icons.Filled.ExitToApp)
}