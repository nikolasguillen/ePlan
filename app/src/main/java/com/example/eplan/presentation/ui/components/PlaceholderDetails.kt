package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
    Column(
        modifier = Modifier.padding(all = 16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Placeholder",
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                        alpha = 0.1F
                    )
                )
                .fillMaxWidth()
                .height(20.dp),
        )
        Text(
            text = "Placeholder",
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                        alpha = 0.1F
                    )
                )
                .fillMaxWidth()
                .height(20.dp),
        )
        Text(
            text = "Placeholder",
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                        alpha = 0.1F
                    )
                )
                .fillMaxWidth()
                .height(20.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Placeholder",
                modifier = Modifier
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                            alpha = 0.1F
                        )
                    )
                    .weight(1F)
                    .height(20.dp),
            )
            Text(
                text = "Placeholder",
                modifier = Modifier
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                            alpha = 0.1F
                        )
                    )
                    .weight(1F)
                    .height(20.dp),
            )
        }
    }
}