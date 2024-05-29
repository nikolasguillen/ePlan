package com.cosmobile.eplan.core.presentation.ui.components.workActivity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.core.util.spacing

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun WorkActivityCard(
    workActivity: WorkActivity,
    onClick: () -> Unit,
    isCompact: Boolean
) {
    val peekColor = try {
        Color(android.graphics.Color.parseColor(workActivity.color))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(MaterialTheme.spacing.small)
                    .background(peekColor)
            )
            if (isCompact) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = MaterialTheme.spacing.small,
                            horizontal = MaterialTheme.spacing.medium
                        )
                ) {
                    Text(
                        text = workActivity.title.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = MaterialTheme.spacing.small)
                            .basicMarquee(Int.MAX_VALUE)
                    )

                    Text(
                        text = "${workActivity.start} - ${workActivity.end}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            } else {
                Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
                    Text(
                        text = workActivity.title.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                    )

                    Text(
                        text = "${workActivity.start} - ${workActivity.end}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall)
                    )

                    if (workActivity.description.isNotBlank()) {
                        Text(
                            text = workActivity.description.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = MaterialTheme.spacing.smallPlus)
                        )
                    }

                }
            }
        }
    }
}