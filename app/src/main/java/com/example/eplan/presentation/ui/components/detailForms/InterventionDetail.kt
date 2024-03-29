package com.example.eplan.presentation.ui.components.detailForms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.eplan.R
import com.example.eplan.presentation.ui.components.uiElements.CustomDateButton
import com.example.eplan.presentation.ui.components.uiElements.CustomTimeButton
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.*
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun InterventionDetail(
    viewModel: InterventionDetailViewModel,
    onActivitySelectionClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    viewModel.intervention.value?.let { intervention ->
        Column {
            Card(modifier = Modifier
                .clickable { onActivitySelectionClick() }
                .fillMaxWidth()
                .wrapContentHeight()) {
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
                    text = if (intervention.activityName == "") stringResource(R.string.premi_selezionare_attivita) else intervention.activityName,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(bottom = MaterialTheme.spacing.medium)
                )
            }
            if (intervention.activityIdError != null) {
                Text(
                    text = intervention.activityIdError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column {
            OutlinedTextField(
                value = intervention.description,
                onValueChange = {
                    viewModel.onFormEvent(DescriptionChanged(it))
                },
                label = { Text(text = stringResource(R.string.descrizione)) },
                modifier = Modifier.fillMaxWidth(),
                isError = intervention.descriptionError != null
            )
            if (intervention.descriptionError != null) {
                Text(
                    text = intervention.descriptionError,
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
                onDateSelected = {
                    viewModel.onFormEvent(DateChanged(it))
                }
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
                    onClick = { time ->
                        viewModel.onFormEvent(StartChanged(time))
                    },
                    modifier = Modifier.weight(4F)
                )
                Spacer(modifier = Modifier.weight(1F))
                CustomTimeButton(
                    time = intervention.end,
                    label = stringResource(R.string.ora_fine),
                    onClick = { time ->
                        viewModel.onFormEvent(EndChanged(time))
                    },
                    modifier = Modifier.weight(4F)
                )
            }
            if (intervention.timeError != null) {
                Text(
                    text = intervention.timeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        OutlinedTextField(
            value = intervention.movingTime,
            onValueChange = { viewModel.onFormEvent(MovingTimeChanged(it)) },
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
            onValueChange = { viewModel.onFormEvent(KmChanged(it)) },
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
    }
}