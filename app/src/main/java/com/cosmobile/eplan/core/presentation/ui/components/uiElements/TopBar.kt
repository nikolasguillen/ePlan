package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onNavigate: () -> Unit) {
    TopAppBar(title = { Text(text = title) },
        actions = {
            IconButton(onClick = { onNavigate() }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.account),
                    modifier = Modifier.size(MaterialTheme.spacing.large)
                )
            }
        })
}