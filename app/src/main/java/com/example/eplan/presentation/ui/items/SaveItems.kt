package com.example.eplan.presentation.ui.items

import com.example.eplan.R

sealed class SaveItems(var route: String, var icon: Int, var title: String) {
    object Save : NavigationItem("save", R.drawable.ic_baseline_save_24, "Salva")
}
