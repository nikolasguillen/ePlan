package com.example.eplan.domain.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Intervention(
    override val id: String = "",
    override val activityId: String = "",
    override val activityName: String = "",
    val activityIdError: String? = null,
    override val title: String = "",
    override val description: String = "",
    val descriptionError: String? = null,
    override val date: LocalDate = LocalDate.now(),
    override val start: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
    override val end: LocalTime = LocalTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
    val timeError: String? = null,
    val movingTime: String = "",
    val km: String = ""
): WorkActivity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Intervention

        if (id != other.id) return false
        if (activityId != other.activityId) return false
        if (activityName != other.activityName) return false
        if (activityIdError != other.activityIdError) return false
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
        var result = id.hashCode()
        result = 31 * result + activityId.hashCode()
        result = 31 * result + activityName.hashCode()
        result = 31 * result + (activityIdError?.hashCode() ?: 0)
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