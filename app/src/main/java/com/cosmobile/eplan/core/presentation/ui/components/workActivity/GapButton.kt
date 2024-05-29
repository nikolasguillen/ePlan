package com.cosmobile.eplan.core.presentation.ui.components.workActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.dashedBorder
import com.cosmobile.eplan.core.util.spacing

@Composable
fun GapButton(
    startTime: String,
    endTime: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .dashedBorder(
                strokeWidth = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                cornerRadiusDp = MaterialTheme.spacing.large
            )
            .clip(RoundedCornerShape(MaterialTheme.spacing.large))
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = startTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = endTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}