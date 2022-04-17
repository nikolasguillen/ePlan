package com.example.eplan.domain.model

data class Appointment(
    val activity: String,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val planning: Boolean,
    val intervention: Boolean,
    val invited: Map<String, Boolean>,
    val periodicity: String,
    val periodicityEnd: String,
    val memo: Boolean,
    val warningTime: String,
    val warningUnit: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appointment

        if (activity != other.activity) return false
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
        var result = activity.hashCode()
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
        result = 31 * result + warningTime.hashCode()
        result = 31 * result + warningUnit.hashCode()
        return result
    }
}