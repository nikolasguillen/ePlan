package com.example.eplan.ui

import com.example.eplan.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

class UIelements {

    @Composable
    fun BuildNavBar() {
        return BottomNavBar()
    }

    @Composable
    fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
    }

    @Composable
    fun BottomNavBar() {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Appointments,
            NavigationItem.Account
        )
        NavigationBar() {
            items.forEach { item ->
                NavigationBarItem(
                    selected = (item.title == "Foglio ore"),
                    onClick = {  },
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    modifier = Modifier.background(
                        colorResource(id = R.color.transparent),
                        CircleShape
                    )
                )
            }
        }
    }
}