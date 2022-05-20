package com.example.eplan.presentation.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.util.getCurrentRoute

@Composable
fun BottomNavBar(navController: NavHostController, items: List<BottomNavBarItems>) {

    val currentRoute = getCurrentRoute(navController = navController)

    NavigationBar(modifier = Modifier.navigationBarsPadding()) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                icon = {
                    Crossfade(targetState = currentRoute == item.route) {
                        if (it) {
                            Icon(imageVector = item.activeIcon, contentDescription = item.title)
                        } else {
                            Icon(imageVector = item.inactiveIcon, contentDescription = item.title)
                        }
                    }
                },
                label = { Text(text = item.title) }
            )
        }
    }
}