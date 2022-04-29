package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.eplan.presentation.util.fromLocalDateToDate
import com.example.eplan.presentation.util.spacing
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun CustomDateButton(date: LocalDate, onDateSelected: (Date) -> Unit) {

    val showDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .clickable { showDialog.value = true }
    )
    {
        Text(
            text = toLiteralDateParser(date.toString()),
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .align(Alignment.CenterHorizontally)
        )
    }

    if (showDialog.value) {
        DatePicker(
            date = fromLocalDateToDate(date),
            onDateSelected = onDateSelected,
            onDismissRequest = { showDialog.value = false }
        )
    }
}