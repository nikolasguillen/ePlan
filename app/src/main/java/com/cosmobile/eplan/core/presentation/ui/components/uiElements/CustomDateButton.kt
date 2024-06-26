package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.core.util.toLiteralDateParser
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@ExperimentalMaterial3Api
@Composable
fun CustomDateButton(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    showLiteralDate: Boolean = true,
    enabled: Boolean = true
) {

    val showDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant()
            .toEpochMilli()
    )

    Card(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = {
                    showDialog.value = true
                },
                role = Role.Button
            )
    )
    {
        Text(
            text = if (showLiteralDate) {
                toLiteralDateParser(date)
            } else {
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            },
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .align(Alignment.CenterHorizontally)
        )
    }

    if (showDialog.value) {

        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(
                                Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                            )
                            showDialog.value = false
                        }
                    }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = stringResource(id = R.string.annulla))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}