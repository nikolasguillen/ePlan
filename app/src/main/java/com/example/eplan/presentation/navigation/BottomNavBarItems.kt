package com.example.eplan.presentation.navigation

import com.example.eplan.R

sealed class BottomNavBarItems(var route: String, var icon: Int, var title: String) {
    object Home : BottomNavBarItems("interventionList", R.drawable.ic_baseline_note_add_24, "Foglio ore")
    object Appointments : BottomNavBarItems("appointmentList", R.drawable.ic_baseline_date_range_24, "Appuntamenti")
    object Save : BottomNavBarItems("save", R.drawable.ic_baseline_save_24, "Salva")
}
