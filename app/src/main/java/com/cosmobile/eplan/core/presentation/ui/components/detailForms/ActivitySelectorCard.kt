package com.cosmobile.eplan.core.presentation.ui.components.detailForms

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.domain.model.Activity
import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.core.util.spacing

@Composable
fun ActivitySelectorCard(
    workActivity: WorkActivity,
    suggestions: List<Activity>,
    onSuggestionSelected: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column {
        Card(
            modifier = modifier.animateContentSize()
        ) {
            Text(
                text = stringResource(id = R.string.attivita),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
            )
            Text(
                text = if (workActivity.activityName == "") stringResource(R.string.premi_selezionare_attivita) else workActivity.activityName,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(bottom = MaterialTheme.spacing.medium)
            )
            if (suggestions.isNotEmpty() && workActivity.activityId.isBlank()) {
                Text(
                    text = stringResource(id = R.string.suggerimenti),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(bottom = MaterialTheme.spacing.extraSmall)
                )

                Column {
                    suggestions.forEachIndexed { index, activity ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSuggestionSelected(activity) }
                                .padding(horizontal = MaterialTheme.spacing.medium)
                        ) {
                            Text(
                                text = activity.name,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(vertical = MaterialTheme.spacing.medium)
                            )
                            if (index < suggestions.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }
            }
            if (workActivity.activityIdError != null) {
                Text(
                    text = workActivity.activityIdError?.asString(context) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}