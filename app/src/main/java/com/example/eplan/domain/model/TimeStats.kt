package com.example.eplan.domain.model

import java.time.LocalDate

data class TimeStats(
    val date: LocalDate,
    val standardTime: Double,
    val overtime: Double,
    val vacation: Double,
    val disease: Double
)