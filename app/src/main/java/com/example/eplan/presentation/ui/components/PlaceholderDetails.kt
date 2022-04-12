package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun PlaceholderDetails() {

    val modifier = Modifier
        .placeholder(
            visible = true,
            highlight = PlaceholderHighlight.shimmer(),
            color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                alpha = 0.1F
            )
        )
        .fillMaxWidth()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier,
            readOnly = true
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier,
            readOnly = true
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier,
            readOnly = true
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Placeholder") },
                modifier = modifier.weight(3F),
                readOnly = true
            )
            Spacer(modifier = Modifier.size(50.dp, 50.dp).weight(1F))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Placeholder") },
                modifier = modifier.weight(3F),
                readOnly = true
            )
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier,
            readOnly = true
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier,
            readOnly = true
        )
    }
}