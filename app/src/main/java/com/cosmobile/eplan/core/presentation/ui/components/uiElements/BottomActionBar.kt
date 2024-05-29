package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.core.presentation.navigation.BottomNavbarAction

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
                        contentDescription = stringResource(id = action.item.titleResId),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = action.item.titleResId),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
        }
    }
}