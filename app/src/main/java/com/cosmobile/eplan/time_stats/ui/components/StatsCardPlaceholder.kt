package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun StatsCardPlaceholder(modifier: Modifier = Modifier) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
    ) {
        // No content
    }
}