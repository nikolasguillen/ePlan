package com.example.eplan.domain.model

import java.time.LocalDate

data class TimeStats(
    val date: LocalDate,
    val standardTime: Int,
    val overtime: Int,
    val vacation: Int,
    val disease: Int
)