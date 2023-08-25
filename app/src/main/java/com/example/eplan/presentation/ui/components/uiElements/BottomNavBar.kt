package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.eplan.presentation.navigation.BottomNavBarItem

@Composable
fun BottomNavBar(
    currentRoute: String,
    items: List<BottomNavBarItem>,
    onNavigate: (String) -> Unit
) {

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Crossfade(targetState = currentRoute == item.route, label = "IconCrossFade") {
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