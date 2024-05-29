package com.cosmobile.eplan.time_stats.ui

import java.time.LocalDate
import java.time.Month

data class TImeStatsUiState(
    val month: Month = LocalDate.now().month,
    val year: Int = LocalDate.now().year,
    val stats: List<WeekStats> = emptyList(),
    val loading: Boolean = true,
    val showAbsentConnectionScreen: Boolean = false
)
