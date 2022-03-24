package com.example.eplan.presentation.ui.items

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar


@Composable
fun SetupCalendar() {

    var day: Day? = null

    val todayBackgroundColor = MaterialTheme.colorScheme.primary.toArgb()
    val todayTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val selectedDayBackgroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val selectedDayTextColor = MaterialTheme.colorScheme.surface.toArgb()
    val dynamicTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                textColor = dynamicTextColor
                setExpandIconColor(dynamicTextColor)

                // Material dynamic theme
                AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                    ?.setTint(selectedDayBackgroundColor)
                selectedItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                selectedItemTextColor = selectedDayTextColor
                AppCompatResources.getDrawable(context, R.drawable.today_circle)
                    ?.setTint(todayBackgroundColor)
                todayItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.today_circle)
                todayItemTextColor = todayTextColor

                setCalendarListener(object : CollapsibleCalendar.CalendarListener {
                    override fun onClickListener() {
                    }

                    override fun onDataUpdate() {
                    }

                    override fun onDayChanged() {
                    }

                    override fun onDaySelect() {
                        day = selectedDay
                        collapse(100)
                    }

                    override fun onItemClick(v: View) {
                    }

                    override fun onMonthChange() {
                    }

                    override fun onWeekChange(position: Int) {
                    }

                })
            }
        },
        modifier = (Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 5.dp))
    )
}

@Composable
fun CustomInputText(
    value: MutableState<String>,
    label: String,
    numField: Boolean = false
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = Color.Transparent,
            disabledTextColor = Color.Transparent
        ),
        singleLine = numField,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = if (numField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputDropDown(
    value: MutableState<String>,
    items: MutableList<String>,
    enabled: MutableState<Boolean>,
    size: Modifier = Modifier
) {
    val showDropDown = remember { mutableStateOf(false) }

    Box {
        OutlinedCard(shape = RoundedCornerShape(8.dp), modifier = size) {
            Row(
                modifier = size.clickable { if (enabled.value) showDropDown.value = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value.value, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp))
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
                Row(modifier = size
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