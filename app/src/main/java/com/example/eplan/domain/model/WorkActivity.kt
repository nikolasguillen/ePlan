package com.example.eplan.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class WorkActivity(
    val idAttivita: String,
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val movingTime: String,
    val km: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkActivity

        if (idAttivita != other.idAttivita) return false
        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (date != other.date) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (movingTime != other.movingTime) return false
        if (km != other.km) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + idAttivita.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + movingTime.hashCode()
        result = 31 * result + km.hashCode()
        return result
    }
}