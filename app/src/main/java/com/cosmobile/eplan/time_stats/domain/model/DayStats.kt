package com.cosmobile.eplan.time_stats.domain.model

import java.time.LocalDate

data class DayStats(
    val date: LocalDate,
    val standardTime: Double,
    val overtime: Double,
    val vacation: Double,
    val disease: Double,
    val isBelowMinimum: Boolean
)