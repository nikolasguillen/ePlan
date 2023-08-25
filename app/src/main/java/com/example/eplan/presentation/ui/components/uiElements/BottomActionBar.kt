package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.eplan.presentation.navigation.BottomNavbarAction

@Composable
fun BottomActionBar(actions: List<BottomNavbarAction>) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        actions.forEach { action ->
            NavigationBarItem(
                selected = false,
                onClick = { action.onClick() },
                icon = {
                    Icon(
                        imageVector = action.item.activeIcon,
                        contentDescription = action.item.title,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                },
                label = {
                    Text(
                        text = action.item.title,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
        }
    }
}