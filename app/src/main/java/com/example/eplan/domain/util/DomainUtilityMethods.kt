package com.example.eplan.domain.util

import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun durationCalculator(start: LocalTime, end: LocalTime): String {
    val diff = Duration.between(start, end).toMinutes()
    return (diff.toFloat()/60.0).toString()
}

fun getDateFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd-MM-yyyy")
}

fun getTimeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ISO_TIME
}