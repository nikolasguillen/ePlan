package com.example.eplan.domain.model

import java.time.LocalDate
import java.time.LocalTime

interface WorkActivity {
    val id: String
    val title: String
    val description: String
    val date: LocalDate
    val start: LocalTime
    val end: LocalTime
}