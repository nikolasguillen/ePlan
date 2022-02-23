package com.example.eplan

sealed class SaveItems(var route: String, var icon: Int, var title: String) {
    object Save : NavigationItem("save", R.drawable.ic_baseline_save_24, "Salva")
    object Cancel : NavigationItem("cancel", R.drawable.ic_baseline_cancel_24, "Scarta modifiche")
}
