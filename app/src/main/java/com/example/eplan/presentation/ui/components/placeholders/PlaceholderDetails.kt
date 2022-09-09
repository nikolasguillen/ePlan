package com.example.eplan.presentation.ui.components.placeholders

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = modifier.weight(4F),
            readOnly = true
        )
        Spacer(modifier = Modifier.weight(1F))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Placeholder") },
            modifier = modifier.weight(4F),
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
