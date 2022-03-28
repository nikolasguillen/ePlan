package com.example.eplan.presentation.ui.items

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.eplan.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputDropDown(
    value: MutableState<String>,
    items: MutableList<String>,
    enabled: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val showDropDown = remember { mutableStateOf(false) }

    Box {
        OutlinedCard(shape = RoundedCornerShape(8.dp), modifier = modifier) {
            Row(
                modifier = modifier.clickable { if (enabled.value) showDropDown.value = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.value,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                )
                IconButton(onClick = { if (enabled.value) showDropDown.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "dropdown"
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
        )
        DropdownMenu(
            expanded = showDropDown.value,
            onDismissRequest = { showDropDown.value = false }
        ) {
            items.forEach {
                Row(modifier = modifier
                    .wrapContentHeight()
                    .clickable {
                        value.value = it
                        showDropDown.value = false
                    }) {
                    Text(text = it, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun CustomSwitch(
    value: MutableState<Boolean> = mutableStateOf(true),
    enabled: MutableState<Boolean> = mutableStateOf(true)
) {
    Switch(
        checked = value.value,
        onCheckedChange = { value.value = it },
        enabled = enabled.value,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

private fun customTimePicker(time: MutableState<String>, context: Context) {

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.MyTimePickerDialogStyle,
        { _, hour: Int, minute: Int ->
            time.value = String.format("%02d", hour) + ":" + String.format("%02d", minute)
        },
        Integer.parseInt(time.value.split(":")[0]),
        Integer.parseInt(time.value.split(":")[1]),
        true
    )


    timePickerDialog.show()

}

private fun customDatePicker(date: MutableState<String>, context: Context) {

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.MyTimePickerDialogStyle,
        { _, year: Int, month: Int, dayOfMonth: Int ->
            date.value =
                "${String.format("%02d", dayOfMonth)}-${String.format("%02d", month + 1)}-${year}"
        },
        Integer.parseInt(date.value.split("-")[2]),
        Integer.parseInt(date.value.split("-")[1]) - 1,
        Integer.parseInt(date.value.split("-")[0])
    )

    datePickerDialog.show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimeButton(time: MutableState<String>, label: String, context: Context) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable { customTimePicker(time, context) }
    )
    {
        Text(
            text = label + ": " + time.value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateButton(date: MutableState<String>, context: Context) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .fillMaxWidth()
            .clickable { customDatePicker(date, context) }
    )
    {
        Text(
            text = date.value,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}