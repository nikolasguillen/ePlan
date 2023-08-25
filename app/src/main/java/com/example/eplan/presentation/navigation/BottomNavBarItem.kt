package com.example.eplan.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavBarItem(var route: String, var inactiveIcon: ImageVector, var activeIcon: ImageVector, var title: String) {
    object Home : BottomNavBarItem("interventionList", Icons.Outlined.NoteAdd, Icons.Filled.NoteAdd, "Foglio ore")
    object Appointments : BottomNavBarItem("appointmentList", Icons.Outlined.CalendarMonth, Icons.Filled.CalendarMonth, "Appuntamenti")
    object Save : BottomNavBarItem("save", Icons.Outlined.Save, Icons.Filled.Save, "Salva")
    object SaveAndClose : BottomNavBarItem("saveAndClose", Icons.Outlined.Save, Icons.Filled.Save, "Salva e chiudi")
    object SaveAndContinue : BottomNavBarItem("saveAndContinue", Icons.Outlined.KeyboardDoubleArrowRight, Icons.Filled.KeyboardDoubleArrowRight, "Salva e continua")
    object Send : BottomNavBarItem("send", Icons.Outlined.Send, Icons.Filled.Send, "Invia")
}
