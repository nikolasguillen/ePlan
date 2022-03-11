package com.example.eplan.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Appointment(
    val activity: String,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val planning: Boolean,
    val intervention: Boolean,
    val invited: List<String>,
    val periodicity: String,
    val periodicityEnd: String,
    val memo: Boolean,
    val warningTime: Int,
    val warningUnit: String
) {
    enum class Periodicity {
        Giornaliera, Settimanale, Bisettimanale, Mensile, Bimestrale
    }
//    val date: LocalDate =
//        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN))
//    val start: LocalTime =
//        LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN))
//    val end: LocalTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN))
}