package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BottomNavBar(navController: NavHostController) {

    val items = listOf(
        BottomNavBarItems.Home,
        BottomNavBarItems.Appointments
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box {

        rememberSystemUiController().setNavigationBarColor(color = Color.Transparent)

        Surface(color = MaterialTheme.colorScheme.surface) {
            rememberSystemUiController().setNavigationBarColor(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.08F
                )
            )
            NavigationBar(modifier = Modifier.navigationBarsPadding()) {
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
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) }
                    )
                }
            }
        }
    }
}