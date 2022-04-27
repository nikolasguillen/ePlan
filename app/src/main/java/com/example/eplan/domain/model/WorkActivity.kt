package com.example.eplan.domain.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class WorkActivity(
    val activityId: String = "",
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val descriptionError: String? = null,
    val date: LocalDate = LocalDate.now(),
    val start: LocalTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute),
    val end: LocalTime = LocalTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
    val timeError: String? = null,
    val movingTime: String = "",
    val km: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkActivity

        if (activityId != other.activityId) return false
        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (descriptionError != other.descriptionError) return false
        if (date != other.date) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (timeError != other.timeError) return false
        if (movingTime != other.movingTime) return false
        if (km != other.km) return false

        return true
    }

    override fun hashCode(): Int {
        var result = activityId.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (descriptionError?.hashCode() ?: 0)
        result = 31 * result + date.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + (timeError?.hashCode() ?: 0)
        result = 31 * result + movingTime.hashCode()
        result = 31 * result + km.hashCode()
        return result
    }
}