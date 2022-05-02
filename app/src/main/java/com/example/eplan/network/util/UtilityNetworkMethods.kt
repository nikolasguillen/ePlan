package com.example.eplan.network.util

import java.time.LocalDate
import java.time.LocalTime

/* Divide il dato da come ci arriva (data + ora in una singola stringa) in un Pair che le contiene separatamente*/
fun dateTimeParser(dateTime: String): Pair<LocalDate, LocalTime> {
    return Pair(
        first = LocalDate.parse(dateTime.split(" ")[0]),
        second = LocalTime.parse(dateTime.split(" ")[1])
    )
}