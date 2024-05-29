package com.cosmobile.eplan.core.presentation.ui.components.placeholders

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.cosmobile.eplan.core.util.spacing
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@ExperimentalMaterial3Api
@Composable
fun PlaceholderCard(
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.1F) else Color.Black.copy(
                    alpha = 0.1F
                )
            )
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(
                text = ""
            )
            Text(
                text = "",
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)
            )
            Text(text = "")
            Text(text = "")
        }
    }

}