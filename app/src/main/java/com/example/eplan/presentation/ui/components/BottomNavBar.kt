package com.example.eplan.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eplan.presentation.navigation.BottomNavBarItems

@Composable
fun BottomNavBar(navController: NavHostController) {

    val items = listOf(
        BottomNavBarItems.Home,
        BottomNavBarItems.Appointments
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route)
                    }
                },
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) }
            )
        }
    }
}