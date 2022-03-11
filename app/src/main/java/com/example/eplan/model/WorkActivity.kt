package com.example.eplan.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class WorkActivity(
    val id: Int,
    val activityName: String,
    val activityDescription: String,
    val date: String,
    val start: String,
    val end: String,
    val movingTime: String,
    val km: String,
    val close: Boolean
) {
//    val date: LocalDate =
//        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN))
//    val start: LocalTime =
//        LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN))
//    val end: LocalTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN))
}