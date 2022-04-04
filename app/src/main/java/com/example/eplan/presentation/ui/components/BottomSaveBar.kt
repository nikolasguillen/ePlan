package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BottomSaveBar(onClick: () -> Unit) {

    val items = listOf(
        BottomNavBarItems.Save,
    )

    Box {

        rememberSystemUiController().setNavigationBarColor(color = MaterialTheme.colorScheme.primary)

        Surface(color = MaterialTheme.colorScheme.primary) {
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
    }
}