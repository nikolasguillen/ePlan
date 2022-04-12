package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.workActivity.ActivityDetailViewModel
import com.example.eplan.presentation.util.fromDateToLocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun WorkActivityDetail(viewModel: ActivityDetailViewModel, workActivity: WorkActivity, bottomPadding: Dp) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = bottomPadding
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = workActivity.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text(text = stringResource(R.string.attivita)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = workActivity.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text(text = stringResource(R.string.descrizione)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomDateButton(
                date = workActivity.date,
                onDateSelected = {
                    viewModel.updateDate(fromDateToLocalDate(it))
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTimeButton(
                startTime = workActivity.start.toString(),
                label = stringResource(R.string.ora_inizio),
                onClick = { time ->
                    viewModel.updateStart(time)
                },
                modifier = Modifier.weight(4F)
            )
            Spacer(modifier = Modifier.weight(1F))
            CustomTimeButton(
                startTime = workActivity.end.toString(),
                label = stringResource(R.string.ora_fine),
                onClick = { time ->
                    viewModel.updateEnd(time)
                },
                modifier = Modifier.weight(4F)
            )
        }
        OutlinedTextField(
            value = workActivity.movingTime,
            onValueChange = { viewModel.updateMovingTime(it) },
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
            onValueChange = { viewModel.updateKm(it) },
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