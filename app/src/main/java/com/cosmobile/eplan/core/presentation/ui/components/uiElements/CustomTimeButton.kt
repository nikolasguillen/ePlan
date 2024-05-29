package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun CustomTimeButton(
    time: LocalTime,
    label: String,
    onClick: (LocalTime) -> Unit,
    modifier: Modifier
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val timePickerState =
        rememberTimePickerState(initialHour = time.hour, initialMinute = time.minute)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable { showDialog = true }
        )
        {
            Text(
                text = "$label: $time",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

    if (showDialog) {
        TimePickerDialog(
            title = stringResource(id = R.string.seleziona_orario),
            onCancel = { showDialog = false },
            onConfirm = {
                onClick(LocalTime.of(timePickerState.hour, timePickerState.minute))
                showDialog = false
            }
        ) {
            androidx.compose.material3.TimePicker(state = timePickerState)
        }
    }
}