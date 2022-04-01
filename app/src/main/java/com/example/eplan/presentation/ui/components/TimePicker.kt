package com.example.eplan.presentation.ui.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.eplan.presentation.util.fromStringToLocalTime
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime

fun showTimePicker(
    inputTime: String,
    context: Context,
    onSelected: (LocalTime) -> Unit
) {
    val activity = context as AppCompatActivity
    val time = fromStringToLocalTime(inputTime)
    val timeBuilder = MaterialTimePicker.Builder()
        .setHour(time.hour)
        .setMinute(time.minute)
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
        .setTimeFormat(TimeFormat.CLOCK_24H)

    val picker = timeBuilder.build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        val outputTime = LocalTime.of(picker.hour, picker.minute)
        onSelected(outputTime)
    }
}