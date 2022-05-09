package com.example.eplan.domain.model

import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Appointment(
    override val id: String = "",
    override val activityId: String = "",
    val activityIdError: String? = null,
    override val title: String = "",
    override val description: String = "",
    val descriptionError: String? = null,
    override val date: LocalDate = LocalDate.now(),
    override val start: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
    override val end: LocalTime = LocalTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
    val timeError: String? = null,
    val planning: Boolean = false,
    val intervention: Boolean = false,
    val invited: List<User> = listOf(),
    val periodicity: Periodicity = Periodicity.NONE,
    val periodicityEnd: LocalDate = LocalDate.now(),
    val memo: Boolean = false,
    // TODO capire come gestire il parametro
    val memoType: List<String> = listOf(),
    val warningTime: Int = 0,
    val warningUnit: WarningUnit = WarningUnit.HOURS
): WorkActivity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appointment

        if (id != other.id) return false
        if (activityId != other.activityId) return false
        if (activityIdError != other.activityIdError) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (descriptionError != other.descriptionError) return false
        if (date != other.date) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (timeError != other.timeError) return false
        if (planning != other.planning) return false
        if (intervention != other.intervention) return false
        if (invited != other.invited) return false
        if (periodicity != other.periodicity) return false
        if (periodicityEnd != other.periodicityEnd) return false
        if (memo != other.memo) return false
        if (memoType != other.memoType) return false
        if (warningTime != other.warningTime) return false
        if (warningUnit != other.warningUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + activityId.hashCode()
        result = 31 * result + (activityIdError?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (descriptionError?.hashCode() ?: 0)
        result = 31 * result + date.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + (timeError?.hashCode() ?: 0)
        result = 31 * result + planning.hashCode()
        result = 31 * result + intervention.hashCode()
        result = 31 * result + invited.hashCode()
        result = 31 * result + periodicity.hashCode()
        result = 31 * result + periodicityEnd.hashCode()
        result = 31 * result + memo.hashCode()
        result = 31 * result + memoType.hashCode()
        result = 31 * result + warningTime
        result = 31 * result + warningUnit.hashCode()
        return result
    }
}