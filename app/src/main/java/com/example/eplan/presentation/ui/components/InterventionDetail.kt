package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.eplan.R
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun InterventionDetail(
    viewModel: InterventionDetailViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    viewModel.intervention.value?.let { intervention ->
        OutlinedTextField(
            value = intervention.activityName,
            onValueChange = { viewModel.onFormEvent(InterventionFormEvent.ActivityNameChanged(it)) },
            label = { Text(text = stringResource(R.string.attivita)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        Column {
            OutlinedTextField(
                value = intervention.description,
                onValueChange = {
                    viewModel.onFormEvent(InterventionFormEvent.DescriptionChanged(it))
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
                    viewModel.onFormEvent(InterventionFormEvent.DateChanged(it))
                }
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomTimeButton(
                    time = intervention.start.toString(),
                    label = stringResource(R.string.ora_inizio),
                    onClick = { time ->
                        viewModel.onFormEvent(InterventionFormEvent.StartChanged(time))
                    },
                    modifier = Modifier.weight(4F)
                )
                Spacer(modifier = Modifier.weight(1F))
                CustomTimeButton(
                    time = intervention.end.toString(),
                    label = stringResource(R.string.ora_fine),
                    onClick = { time ->
                        viewModel.onFormEvent(InterventionFormEvent.EndChanged(time))
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
            onValueChange = { viewModel.onFormEvent(InterventionFormEvent.MovingTimeChanged(it)) },
            label = { Text(text = stringResource(R.string.ore_spostamento)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
        OutlinedTextField(
            value = intervention.km,
            onValueChange = { viewModel.onFormEvent(InterventionFormEvent.KmChanged(it)) },
            label = { Text(text = stringResource(R.string.km_percorsi)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
    }
}