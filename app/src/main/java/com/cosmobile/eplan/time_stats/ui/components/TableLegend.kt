package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing

@Composable
fun TableLegend(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
    ) {
        stringArrayResource(id = R.array.tabella_stats_header).zip(stringArrayResource(id = R.array.tabella_stats_legenda))
            .forEach {
                TableLegendRow(
                    legend = it.first,
                    explanation = it.second,
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}

@Composable
private fun TableLegendRow(
    legend: String,
    explanation: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = "• $legend: ", fontWeight = FontWeight.Bold)
        Text(text = explanation)
    }
}