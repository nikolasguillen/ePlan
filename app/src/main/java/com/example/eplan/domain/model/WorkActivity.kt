package com.example.eplan.domain.model

import java.time.LocalDate

data class WorkActivity(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val start: String,
    val end: String,
    val movingTime: String,
    val km: String
)