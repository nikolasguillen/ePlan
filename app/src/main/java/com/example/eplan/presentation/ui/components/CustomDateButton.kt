package com.example.eplan.presentation.ui.components

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.fromLocalDateToDate
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateButton(date: LocalDate, onDateSelected: (Date) -> Unit) {

    val showDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .fillMaxWidth()
            .clickable { showDialog.value = true }
    )
    {
        Text(
            text = toLiteralDateParser(date.toString()),
            modifier = Modifier
                .padding(16.dp)
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