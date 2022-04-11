package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.eplan.presentation.navigation.BottomNavBarItems

@Composable
fun BottomSaveBar(onClick: () -> Unit) {

    val items = listOf(
        BottomNavBarItems.Save,
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { onClick() },
                icon = {
                    Icon(
                        painterResource(id = item.icon),
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


}