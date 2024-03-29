package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navigate: () -> Unit) {
    TopAppBar(title = { Text(text = title) },
        actions = {
            IconButton(onClick = { navigate() }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.account),
                    modifier = Modifier.size(MaterialTheme.spacing.large)
                )
            }
        })
}