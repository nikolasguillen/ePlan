package com.example.eplan.presentation.navigation

data class BottomNavbarAction(
    val item: BottomNavBarItem,
    val onClick: () -> Unit
)
