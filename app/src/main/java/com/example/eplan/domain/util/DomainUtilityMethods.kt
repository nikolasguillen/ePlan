package com.example.eplan.domain.util

import java.time.Duration
import java.time.LocalTime

fun durationCalculator(start: LocalTime, end: LocalTime): String {
    val diff = Duration.between(start, end).toMinutes()
    return (diff.toFloat()/60.0).toString()
}