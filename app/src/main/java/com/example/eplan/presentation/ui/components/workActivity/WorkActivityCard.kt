package com.example.eplan.presentation.ui.components.workActivity

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@Composable
fun WorkActivityCard(
    workActivity: WorkActivity,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(
                text = workActivity.title.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (workActivity.description.isNotBlank()) {
                Text(
                    text = workActivity.description.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)
                )
            }
            Text(text = workActivity.start.toString(), style = MaterialTheme.typography.labelSmall)
            Text(text = workActivity.end.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}