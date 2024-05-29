package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cosmobile.eplan.core.util.spacing
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun StatsHeader(
    currentDate: String,
    totalHours: String,
    standardHours: String,
    overtimeHours: String,
    vacationHours: String,
    diseaseHours: String,
    onDateButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    shouldShowPlaceholders: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        modifier = modifier
    ) {
        TextButton(
            onClick = onDateButtonClick,
            modifier = Modifier.padding(MaterialTheme.spacing.small)
        ) {
            Text(
                text = currentDate,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Icon(imageVector = Icons.Default.EditCalendar, contentDescription = null)
        }
        Text(
            text = totalHours,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.placeholder(
                visible = shouldShowPlaceholders,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
        )
        Text(
            text = standardHours,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.placeholder(
                visible = shouldShowPlaceholders,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
        )
        Text(
            text = overtimeHours,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.placeholder(
                visible = shouldShowPlaceholders,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
        )
        Text(
            text = vacationHours,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.placeholder(
                visible = shouldShowPlaceholders,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
        )
        Text(
            text = diseaseHours,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.placeholder(
                visible = shouldShowPlaceholders,
                highlight = PlaceholderHighlight.shimmer(),
                color = if (isSystemInDarkTheme()) {
                    Color.White.copy(alpha = 0.1F)
                } else {
                    Color.Black.copy(
                        alpha = 0.1F
                    )
                }
            )
        )
    }
}