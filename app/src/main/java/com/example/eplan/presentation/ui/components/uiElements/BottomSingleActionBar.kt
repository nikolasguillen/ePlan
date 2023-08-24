package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.eplan.presentation.navigation.BottomNavBarItems

@Composable
fun BottomSingleActionBar(item: BottomNavBarItems, onClick: () -> Unit) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
//        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { onClick() },
            icon = {
                Icon(
                    imageVector = item.activeIcon,
                    contentDescription = item.title,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            label = {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        )
    }


}