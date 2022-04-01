package com.example.eplan.presentation.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

fun fromLocalDateToDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun fromDateToLocalDate(date: Date): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun fromLocalTimeToString(localTime: LocalTime): String {
    return String.format("%02d", localTime.hour) + ":" + String.format("%02d", localTime.minute)
}

fun fromStringToLocalTime(timeString: String): LocalTime {
    val hour = timeString.split(":")[0].toInt()
    val minute = timeString.split(":")[1].toInt()

    return LocalTime.of(hour, minute)
}