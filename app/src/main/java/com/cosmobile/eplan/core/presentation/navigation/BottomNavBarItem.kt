package com.cosmobile.eplan.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.Save
import androidx.compose.ui.graphics.vector.ImageVector
import com.cosmobile.eplan.R

sealed class BottomNavBarItem(
    val route: String? = null,
    val inactiveIcon: ImageVector,
    val activeIcon: ImageVector,
    @StringRes val titleResId: Int
) {
    data object Home : BottomNavBarItem(
        route = ScreenRoutes.INTERVENTION_LIST,
        inactiveIcon = Icons.AutoMirrored.Outlined.NoteAdd,
        activeIcon = Icons.AutoMirrored.Filled.NoteAdd,
        titleResId = R.string.foglio_ore_label
    )

    data object Appointments : BottomNavBarItem(
        route = ScreenRoutes.APPOINTMENT_LIST,
        inactiveIcon = Icons.Outlined.CalendarMonth,
        activeIcon = Icons.Filled.CalendarMonth,
        titleResId = R.string.appuntamenti_label
    )

    data object Save : BottomNavBarItem(
        inactiveIcon = Icons.Outlined.Save,
        activeIcon = Icons.Filled.Save,
        titleResId = R.string.salva_label
    )

    data object SaveAndClose : BottomNavBarItem(
        inactiveIcon = Icons.Outlined.Save,
        activeIcon = Icons.Filled.Save,
        titleResId = R.string.salva_e_chiudi_label
    )

    data object SaveAndContinue : BottomNavBarItem(
        inactiveIcon = Icons.Outlined.KeyboardDoubleArrowRight,
        activeIcon = Icons.Filled.KeyboardDoubleArrowRight,
        titleResId = R.string.salva_e_continua_label
    )

    data object Send : BottomNavBarItem(
        inactiveIcon = Icons.AutoMirrored.Outlined.Send,
        activeIcon = Icons.AutoMirrored.Filled.Send,
        titleResId = R.string.invia_label
    )
}
