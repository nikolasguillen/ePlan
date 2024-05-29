package com.cosmobile.eplan.core.presentation.ui.components.detailForms

import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnActivitySelected
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnToggleActivitySelectorVisibility
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomDateButton
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomTimeButton
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailUiState
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnDateChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnDescriptionUpdated
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnEndTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnKmChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnMovingTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnStartTimeChanged
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionFormEvent.OnToggleSuggestionsVisibility

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun InterventionDetail(
    state: InterventionDetailUiState,
    onFormEvent: (WorkActivityFormEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var descriptionTextFieldHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current


    state.workActivity?.let { intervention ->
        ActivitySelectorCard(
            workActivity = intervention,
            suggestions = state.activitySelectorUiState.activities.take(5),
            onSuggestionSelected = { onFormEvent(OnActivitySelected(it)) },
            modifier = Modifier
                .clickable { onFormEvent(OnToggleActivitySelectorVisibility) }
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Column {
            Box {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.showDescriptionSuggestions,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                    modifier = Modifier.padding(top = max(0.dp, descriptionTextFieldHeight - 8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                    ) {
                        if (state.descriptionSuggestions.isNotEmpty()) {
                            Text(
                                text = stringResource(id = R.string.suggerimenti),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = MaterialTheme.spacing.medium,
                                    vertical = MaterialTheme.spacing.small
                                )
                            )
                        }
                        LazyColumn(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            if (state.descriptionSuggestions.isNotEmpty()) {
                                items(items = state.descriptionSuggestions) { suggestion ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .clickable {
                                                onFormEvent(OnDescriptionUpdated(suggestion))
                                                onFormEvent(OnToggleSuggestionsVisibility)
                                            }
                                    ) {
                                        Text(
                                            text = suggestion,
                                            style = MaterialTheme.typography.labelLarge,
                                            maxLines = 1,
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = MaterialTheme.spacing.medium,
                                                    vertical = MaterialTheme.spacing.small
                                                )
                                                .basicMarquee(iterations = Int.MAX_VALUE)
                                        )
                                    }
                                }
                            } else {
                                item {
                                    if (intervention.activityId.isNotBlank()) {
                                        Text(
                                            text = stringResource(id = R.string.nessun_suggerimento),
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(
                                                horizontal = MaterialTheme.spacing.medium,
                                                vertical = MaterialTheme.spacing.medium
                                            )
                                        )
                                    } else {
                                        Text(
                                            text = stringResource(id = R.string.seleziona_attivita_per_suggerimenti),
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(
                                                horizontal = MaterialTheme.spacing.medium,
                                                vertical = MaterialTheme.spacing.medium
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = intervention.description,
                    onValueChange = { onFormEvent(OnDescriptionUpdated(it)) },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onFormEvent(OnToggleSuggestionsVisibility)
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = stringResource(id = R.string.suggerimenti)
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        errorContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    label = { Text(text = stringResource(R.string.descrizione)) },
                    isError = intervention.descriptionError != null,
                    keyboardOptions = KeyboardOptions(
                        capitalization =
                        KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            with(density) { descriptionTextFieldHeight = it.size.height.toDp() }
                        }
                )
            }
            if (intervention.descriptionError != null) {
                Text(
                    text = intervention.descriptionError.asString(context),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            CustomDateButton(
                date = intervention.date,
                onDateSelected = { onFormEvent(OnDateChanged(it)) }
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomTimeButton(
                    time = intervention.start,
                    label = stringResource(R.string.ora_inizio),
                    onClick = { time -> onFormEvent(OnStartTimeChanged(time)) },
                    modifier = Modifier.weight(4F)
                )
                Spacer(modifier = Modifier.weight(1F))
                CustomTimeButton(
                    time = intervention.end,
                    label = stringResource(R.string.ora_fine),
                    onClick = { time -> onFormEvent(OnEndTimeChanged(time)) },
                    modifier = Modifier.weight(4F)
                )
            }
            if (intervention.timeError != null) {
                Text(
                    text = intervention.timeError.asString(context),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.small)
                )
            }
        }
        OutlinedTextField(
            value = intervention.movingTime,
            onValueChange = { onFormEvent(OnMovingTimeChanged(it)) },
            label = { Text(text = stringResource(R.string.ore_spostamento)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = intervention.km,
            onValueChange = { onFormEvent(OnKmChanged(it)) },
            label = { Text(text = stringResource(R.string.km_percorsi)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}