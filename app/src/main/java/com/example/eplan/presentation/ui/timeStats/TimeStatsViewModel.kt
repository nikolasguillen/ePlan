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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TimeStatsViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getTimeStats: GetTimeStats
) : EplanViewModel() {

    var date: LocalDate = LocalDate.now()
    var stats = mutableStateListOf<TimeStats>()
        private set

    private val validationEventChannel = Channel<String>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        for (i in 1..30) {
            stats.add(TimeStats(LocalDate.of(2022, 6, i), i, i, i, i))
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
        getTimeStats.execute(token = userToken, month = date.monthValue, year = date.year).onEach { dataState ->
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
}