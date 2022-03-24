package com.example.eplan.presentation.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.R

@Composable
fun TopBar(title: String, navigate: () -> Unit) {
    SmallTopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { navigate() }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.account),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}