package com.cosmobile.eplan.core.presentation.ui.components.workActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.core.util.spacing

@Composable
fun CompletedDayCard(modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Column {
                Text(
                    text = "Giornata completata!",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                Text(
                    text = "Hai completato le 8 ore previste.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}