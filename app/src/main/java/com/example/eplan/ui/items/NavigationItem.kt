package com.example.eplan.ui.items

import com.example.eplan.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_baseline_note_add_24, "Foglio ore")
    object Appointments : NavigationItem("appointments", R.drawable.ic_baseline_date_range_24, "Appuntamenti")
}
