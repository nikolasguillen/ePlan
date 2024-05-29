package com.cosmobile.eplan.time_stats.ui

sealed class TimeStatsEvent {
    data object RefreshStats : TimeStatsEvent()
    data class UpdateDate(val month: Int, val year: Int) : TimeStatsEvent()
}
