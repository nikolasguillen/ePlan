package com.cosmobile.eplan.core.presentation.navigation

data class BottomNavbarAction(
    val item: BottomNavBarItem,
    val onClick: () -> Unit
)
