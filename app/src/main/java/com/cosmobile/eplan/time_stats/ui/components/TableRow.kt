package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.time_stats.domain.model.DayStats
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TableRow(
    dayStats: DayStats,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = dayStats.date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (dayStats.isBelowMinimum) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dayStats.date.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (dayStats.isBelowMinimum) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = dayStats.date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = if (dayStats.isBelowMinimum) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TableRowItem(
            value = dayStats.standardTime,
            isError = dayStats.isBelowMinimum,
            modifier = Modifier.weight(1f)
        )
        TableRowItem(
            value = dayStats.overtime,
            isError = dayStats.isBelowMinimum,
            modifier = Modifier.weight(1f)
        )
        TableRowItem(
            value = dayStats.vacation,
            isError = dayStats.isBelowMinimum,
            modifier = Modifier.weight(1f)
        )
        TableRowItem(
            value = dayStats.disease,
            isError = dayStats.isBelowMinimum,
            modifier = Modifier.weight(1f)
        )
    }

    if (dayStats.isBelowMinimum) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spacing.extraSmall,
                    horizontal = MaterialTheme.spacing.small
                )
        ) {
            Text(
                text = stringResource(R.string.controlla_ore),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.End
            )
        }
    }
}