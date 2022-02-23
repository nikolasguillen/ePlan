package com.example.eplan

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_baseline_note_add_24, "Foglio ore")
    object Appointments : NavigationItem("appointments", R.drawable.ic_baseline_date_range_24, "Appuntamenti")
    object Account : NavigationItem("account", R.drawable.ic_baseline_account_circle_24, "Account")
}
