package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.eplan.presentation.navigation.BottomNavBarItems

@Composable
fun BottomSingleActionBar(item: BottomNavBarItems, onClick: () -> Unit) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.navigationBarsPadding()
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