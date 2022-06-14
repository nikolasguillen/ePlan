package com.example.eplan.presentation.ui.timeStats

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.TimeStats
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.timeStats.GetTimeStats
import com.example.eplan.network.model.TimeStatsDto
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.timeStats.TimeStatsEvent.*
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.temporal.WeekFields
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
class TimeStatsViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getTimeStats: GetTimeStats
) : EplanViewModel() {

    var date: LocalDate = LocalDate.now()
    private var stats = mutableStateListOf<TimeStats>()
    val columnHeaders = mapOf(
        "#" to "Numero del giorno",
        "G" to "Giorno della settimana",
        "N" to "Ore normali",
        "E" to "Straordinari",
        "M" to "Ore di malattia",
        "P" to "Ore di permesso"
    )

    private val validationEventChannel = Channel<String>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        for (i in 1..date.lengthOfMonth()) {
            stats.add(
                TimeStats(
                    LocalDate.of(date.year, date.month, i),
                    if (i % 2 == 0) (Random.nextDouble(8.0) * 100).roundToInt() / 100.0 else 8.0,
                    0.0,
                    0.0,
                    0.0
                )
            )
        }
        getToken(getToken = getToken, onTokenRetrieved = { onTriggerEvent(GetStats) })
    }

    fun onTriggerEvent(event: TimeStatsEvent) {
        when (event) {
            is GetStats -> {
                getStats()
            }
        }
    }

    private fun getStats() {
        getTimeStats.execute(token = userToken, month = date.monthValue, year = date.year)
            .onEach { dataState ->
                retrieving = dataState.loading

                dataState.data?.let { retrievedStats ->
                    stats.clear()
                    stats.addAll(retrievedStats)
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getTimeStats (T.S.V.M.): $error")
                }
            }.launchIn(viewModelScope)
    }

    fun getWeekSortedStats(): List<Pair<Int, List<TimeStats>>> {
        return stats.groupBy { it.date.get(WeekFields.ISO.weekOfWeekBasedYear()) }.toList()
    }

    fun checkMinimumTime(date: LocalDate): Boolean {
        return getDayTotalTime(date) >= 8
    }

    fun getWeekTotalHours(weekNumber: Int): Double {
        stats.groupBy { it.date.get(WeekFields.ISO.weekOfWeekBasedYear()) }[weekNumber].let { statsList ->
            var total = 0.0
            statsList?.forEach { total += getDayTotalTime(it.date) }
            return (total * 100).roundToInt() / 100.0
        }
    }

    private fun getDayTotalTime(date: LocalDate): Double {
        stats.first { it.date == date }.let {
            return it.standardTime + it.overtime + it.disease + it.vacation
        }
    }
}