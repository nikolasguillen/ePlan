package com.example.eplan.domain.model

import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Appointment(
    val id: String = "",
    val activityId: String = "",
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val start: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
    val end: LocalTime = LocalTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
    val planning: Boolean = false,
    val intervention: Boolean = false,
    val invited: Map<User, Boolean>,
    val periodicity: Periodicity = Periodicity.NESSUNA,
    val periodicityEnd: LocalDate = LocalDate.now(),
    val memo: Boolean = false,
    // TODO capire come gestire il parametro
    val memoType: List<String> = listOf(),
    val warningTime: Int,
    val warningUnit: WarningUnit
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appointment

        if (id != other.id) return false
        if (activityId != other.activityId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (date != other.date) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (planning != other.planning) return false
        if (intervention != other.intervention) return false
        if (invited != other.invited) return false
        if (periodicity != other.periodicity) return false
        if (periodicityEnd != other.periodicityEnd) return false
        if (memo != other.memo) return false
        if (warningTime != other.warningTime) return false
        if (warningUnit != other.warningUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + activityId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + planning.hashCode()
        result = 31 * result + intervention.hashCode()
        result = 31 * result + invited.hashCode()
        result = 31 * result + periodicity.hashCode()
        result = 31 * result + periodicityEnd.hashCode()
        result = 31 * result + memo.hashCode()
        result = 31 * result + warningTime
        result = 31 * result + warningUnit.hashCode()
        return result
    }
}