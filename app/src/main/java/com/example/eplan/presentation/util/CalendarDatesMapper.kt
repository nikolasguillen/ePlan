package com.example.eplan.presentation.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun fromLocalDateToDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun fromDateToLocalDate(date: Date): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}