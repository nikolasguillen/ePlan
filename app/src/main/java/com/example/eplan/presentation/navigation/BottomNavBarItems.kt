package com.example.eplan.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Send
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavBarItems(var route: String, var inactiveIcon: ImageVector, var activeIcon: ImageVector, var title: String) {
    object Home : BottomNavBarItems("interventionList", Icons.Outlined.NoteAdd, Icons.Filled.NoteAdd, "Foglio ore")
    object Appointments : BottomNavBarItems("appointmentList", Icons.Outlined.DateRange, Icons.Filled.DateRange, "Appuntamenti")
    object Save : BottomNavBarItems("save", Icons.Outlined.Save, Icons.Filled.Save, "Salva")
    object Send : BottomNavBarItems("send", Icons.Outlined.Send, Icons.Filled.Send, "Invia")
}
