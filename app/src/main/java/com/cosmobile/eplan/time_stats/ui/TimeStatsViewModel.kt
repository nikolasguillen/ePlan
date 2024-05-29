package com.cosmobile.eplan.time_stats.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.presentation.EplanViewModel
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.NetworkUtils
import com.cosmobile.eplan.time_stats.domain.use_cases.GetInterventionsForMonth
import com.cosmobile.eplan.time_stats.ui.TimeStatsEvent.RefreshStats
import com.cosmobile.eplan.time_stats.ui.TimeStatsEvent.UpdateDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Month
import javax.inject.Inject

@HiltViewModel
class TimeStatsViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getInterventionsForMonth: GetInterventionsForMonth,
    private val networkUtils: NetworkUtils
) : EplanViewModel() {
    private val _state = MutableStateFlow(TImeStatsUiState())
    val state = _state.asStateFlow()

    private val validationEventChannel = Channel<String>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onEvent(RefreshStats) })
    }

    fun onEvent(event: TimeStatsEvent) {
        when (event) {
            is RefreshStats -> {
                retrieveStats()
            }

            is UpdateDate -> {
                _state.update {
                    it.copy(month = Month.of(event.month), year = event.year)
                }.let {
                    retrieveStats()
                }
            }
        }
    }

    private fun retrieveStats() {
        if (networkUtils.isConnectionAvailable()) {
            getInterventionsForMonth.execute(
                token = userToken,
                month = state.value.month.value,
                year = state.value.year
            )
                .onEach { dataState ->
                    _state.update {
                        it.copy(loading = dataState.loading)
                    }

                    dataState.data?.let { retrievedStats ->
                        _state.update {
                            it.copy(
                                stats = retrievedStats,
                                loading = false,
                                showAbsentConnectionScreen = false
                            )
                        }
                    }

                    dataState.error?.let { error ->
                        Log.e(GENERIC_DEBUG_TAG, "getTimeStats (T.S.V.M.): $error")
                        _state.update { it.copy(loading = false) }
                    }
                }.launchIn(viewModelScope)
        } else {
            _state.update { it.copy(showAbsentConnectionScreen = true, loading = false) }
        }
    }
}