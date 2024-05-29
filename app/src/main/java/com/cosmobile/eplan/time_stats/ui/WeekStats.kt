package com.cosmobile.eplan.time_stats.ui

import com.cosmobile.eplan.time_stats.domain.model.DayStats
import kotlin.math.roundToInt

data class WeekStats(
    val days: List<DayStats>,
    val weekNumber: Int
) {
    fun getTotalHours(): Double {
        val total = days.sumOf { it.standardTime + it.overtime + it.vacation + it.disease }
        return (total * 100).roundToInt() / 100.0
    }

    fun getTotalStandardTime(): Double {
        val total = days.sumOf { it.standardTime }
        return (total * 100).roundToInt() / 100.0
    }

    fun getTotalOvertime(): Double {
        val total = days.sumOf { it.overtime }
        return (total * 100).roundToInt() / 100.0
    }

    fun getTotalVacation(): Double {
        val total = days.sumOf { it.vacation }
        return (total * 100).roundToInt() / 100.0
    }

    fun getTotalDisease(): Double {
        val total = days.sumOf { it.disease }
        return (total * 100).roundToInt() / 100.0
    }
}