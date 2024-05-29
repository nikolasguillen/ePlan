package com.cosmobile.eplan.core.data

import java.time.LocalDate
import java.time.LocalTime

/** Divide il dato da come ci arriva (data + ora in una singola stringa) in un Pair che le contiene separatamente **/
fun dateTimeParser(dateTime: String): Pair<LocalDate, LocalTime> {
    return Pair(
        first = LocalDate.parse(dateTime.split(" ")[0]),
        second = LocalTime.parse(dateTime.split(" ")[1])
    )
}

/** Compose mostra il tag HTML per l'andata a capo, quindi tocca levarlo manualmente*/
fun removeHtmlBreak(text: String): String {
    return text
        .replace(oldValue = "<br />", newValue = "")
        .replace(oldValue = "<br/>", newValue = "")
}