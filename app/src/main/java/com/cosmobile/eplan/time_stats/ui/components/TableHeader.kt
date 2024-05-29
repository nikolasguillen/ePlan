package com.cosmobile.eplan.time_stats.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing

@Composable
fun TableHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.extraSmall
            )
    ) {
        stringArrayResource(id = R.array.tabella_stats_header).forEach {
            TableHeaderItem(
                text = it,
                modifier = Modifier.weight(1f)
            )
        }
    }
}