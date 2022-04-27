package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.ui.workActivity.ActivityFormEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun WorkActivityDetail(
    viewModel: ActivityDetailViewModel,
    topPadding: Dp,
    bottomPadding: Dp
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    viewModel.workActivity.value?.let { workActivity ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = topPadding,
                    bottom = bottomPadding
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = workActivity.title,
                onValueChange = { viewModel.onFormEvent(ActivityFormEvent.TitleChanged(it)) },
                label = { Text(text = stringResource(R.string.attivita)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Column {
                OutlinedTextField(
                    value = workActivity.description,
                    onValueChange = {
                        viewModel.onFormEvent(ActivityFormEvent.DescriptionChanged(it))
                    },
                    label = { Text(text = stringResource(R.string.descrizione)) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (workActivity.descriptionError != null) {
                    Text(
                        text = workActivity.descriptionError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(
                            Alignment.End
                        )
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomDateButton(
                    date = workActivity.date,
                    onDateSelected = {
                        viewModel.onFormEvent(ActivityFormEvent.DateChanged(it))
                    }
                )
            }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomTimeButton(
                        time = workActivity.start.toString(),
                        label = stringResource(R.string.ora_inizio),
                        onClick = { time ->
                            viewModel.onFormEvent(ActivityFormEvent.StartChanged(time))
                        },
                        modifier = Modifier.weight(4F)
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    CustomTimeButton(
                        time = workActivity.end.toString(),
                        label = stringResource(R.string.ora_fine),
                        onClick = { time ->
                            viewModel.onFormEvent(ActivityFormEvent.EndChanged(time))
                        },
                        modifier = Modifier.weight(4F)
                    )
                }
                if (workActivity.timeError != null) {
                    Text(
                        text = workActivity.timeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(
                            Alignment.End
                        )
                    )
                }
            }
            OutlinedTextField(
                value = workActivity.movingTime,
                onValueChange = { viewModel.onFormEvent(ActivityFormEvent.MovingTimeChanged(it)) },
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
                value = workActivity.km,
                onValueChange = { viewModel.onFormEvent(ActivityFormEvent.KmChanged(it)) },
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
}