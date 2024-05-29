package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.time_stats.ui.WeekStats

@Composable
fun WeekStatsCard(
    weekStats: WeekStats,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.settimana) + " " + weekStats.weekNumber,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface
        )

        TableHeader()

        weekStats.days.forEach {
            TableRow(
                dayStats = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.extraSmall,
                        vertical = MaterialTheme.spacing.small
                    )
            )
            if (weekStats.days.indexOf(it) != weekStats.days.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black.copy(alpha = 0.2F)
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(MaterialTheme.spacing.small)
        ) {
            Text(
                text = stringResource(
                    R.string.bilancio_settimanale,
                    weekStats.getTotalHours().toString()
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiary,
                maxLines = 1
            )
        }
    }
}