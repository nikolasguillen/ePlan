package com.example.eplan.network.util

import java.time.LocalDate
import java.time.LocalTime

/* Divideil dato da come ci arriva (data + ora in una singola stringa) in un Pair che le contiene separatamente*/
fun dateTimeParser(dateTime: String): Pair<LocalDate, LocalTime> {
    return Pair(
        first = dateFromNetwork(dateTime.split(" ")[0]),
        second = LocalTime.parse(dateTime.split(" ")[1])
    )
}

/* Converte la data dal formato yyyy-MM-dd a dd-MM-yyyy */
fun dateFromNetwork(date: String): LocalDate {
    val dayOfMonth = date.split("-")[2].toInt()
    val month = date.split("-")[1].toInt()
    val year = date.split("-")[0].toInt()

    return LocalDate.of(year, month, dayOfMonth)
}

/* Converte la data dal formato dd-MM-yyyy a yyyy-MM-dd */
fun dateToNetwork(date: LocalDate): String {
    val dayOfMonth = date.dayOfMonth
    val month = date.monthValue
    val year = date.year

    return "$year-$month-$dayOfMonth"
}