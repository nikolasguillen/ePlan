package com.cosmobile.eplan.interventions_list.ui

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.core.domain.use_cases.ClearUserData
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.domain.util.calculateGaps
import com.cosmobile.eplan.core.domain.util.removeGaps
import com.cosmobile.eplan.core.presentation.EplanViewModel
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.NetworkUtils
import com.cosmobile.eplan.core.util.SHOW_GAPS_SUGGESTIONS
import com.cosmobile.eplan.core.util.WORK_END_TIME
import com.cosmobile.eplan.core.util.WORK_START_TIME
import com.cosmobile.eplan.interventions_list.domain.use_cases.DayChangeIntervention
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.DayChangeEvent
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnDecreaseDate
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnIncreaseDate
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleCalendarState
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleListState
import com.cosmobile.eplan.interventions_list.ui.InterventionListEvent.OnToggleVisualizationMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class InterventionListViewModel
@Inject
constructor(
    getToken: GetToken,
    private val context: Application,
    private val dayChangeIntervention: DayChangeIntervention,
    private val clearUserData: ClearUserData,
    private val savedStateHandle: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
    private val networkUtils: NetworkUtils
) : EplanViewModel() {

    var state by mutableStateOf(InterventionListUiState())
        private set

    private val _logoutChannel = Channel<Unit>()
    val logoutChannel = _logoutChannel.receiveAsFlow()

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onEvent(event: InterventionListEvent) {
        when (event) {
            is DayChangeEvent -> {
                if (!event.forceRefresh && event.date != null && event.date == state.selectedDate) {
                    state = state.copy(isCalendarExpanded = false)
                    return
                }

                setDate(date = event.date ?: state.selectedDate)
                dayChange(forceRefresh = event.forceRefresh)
            }

            is OnToggleCalendarState -> {
                state = state.copy(isCalendarExpanded = !state.isCalendarExpanded)
            }

            is OnToggleListState -> {
                state = state.copy(isListCollapsed = !state.isListCollapsed)
            }

            is OnToggleVisualizationMode -> {
                val newDisplayMode = when (state.displayMode) {
                    DisplayMode.DAILY -> DisplayMode.WEEKLY
                    DisplayMode.WEEKLY -> DisplayMode.DAILY
                }
                state = state.copy(displayMode = newDisplayMode)
                dayChange(forceRefresh = true)
            }

            OnIncreaseDate -> {
                when (state.displayMode) {
                    DisplayMode.DAILY -> setDate(state.selectedDate.plusDays(1))
                    DisplayMode.WEEKLY -> setDate(state.selectedDate.plusDays(7))
                }
                dayChange()
            }

            OnDecreaseDate -> {
                when (state.displayMode) {
                    DisplayMode.DAILY -> setDate(state.selectedDate.minusDays(1))
                    DisplayMode.WEEKLY -> setDate(state.selectedDate.minusDays(7))
                }
                dayChange()
            }
        }
    }

    private fun onCreation() {
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setDate(LocalDate.parse(q))
        }
        dayChange()
    }

    private fun dayChange(forceRefresh: Boolean = false) {
        state = state.copy(isCalendarExpanded = false)

        if (forceRefresh.not() && state.displayMode == DisplayMode.WEEKLY && state.interventions.isNotEmpty()) {
            val currentStartDate =
                state.interventions.entries.firstOrNull()?.value?.firstOrNull()?.date?.with(
                    DayOfWeek.MONDAY
                ) ?: return
            val currentEndDate = currentStartDate.with(DayOfWeek.SUNDAY)

            if (
                (state.selectedDate.isAfter(currentStartDate)
                        && state.selectedDate.isBefore(currentEndDate))
                || state.selectedDate == currentStartDate
            ) {
                return
            }
        }

        if (networkUtils.isConnectionAvailable()) {
            val (startDate, endDate) = when (state.displayMode) {
                DisplayMode.DAILY -> {
                    Pair(state.selectedDate, state.selectedDate)
                }

                DisplayMode.WEEKLY -> {
                    // should select the start and end date of the week based on the selected date
                    Pair(
                        state.selectedDate.with(DayOfWeek.MONDAY),
                        state.selectedDate.with(DayOfWeek.SUNDAY)
                    )
                }
            }
            dayChangeIntervention.execute(
                token = userToken,
                startDate = startDate,
                endDate = endDate
            )
                .onEach { dataState ->
                    state = state.copy(isRefreshing = dataState.loading)

                    dataState.data?.let { list ->

                        state = state.copy(
                            interventions = list.groupByCurrentDisplayMode(),
                            showAbsentConnectionScreen = false,
                            isDayCompleted = when (state.displayMode) {
                                DisplayMode.DAILY -> list.sumOf {
                                    ChronoUnit.MINUTES.between(
                                        it.start,
                                        it.end
                                    )
                                } >= (8 * 60)

                                DisplayMode.WEEKLY -> false
                            }
                        )
                    }

                    dataState.error?.let { error ->
                        Log.e(GENERIC_DEBUG_TAG, "dayChange (workActivity): $error")
                        state = state.copy(showAbsentConnectionScreen = false)
                        if (error.asString(context) == "HTTP 401 Unauthorized") {
                            clearUserData { viewModelScope.launch { _logoutChannel.send(Unit) } }
                        }
                    }
                }.launchIn(viewModelScope)
        } else {
            state = state.copy(
                isRefreshing = false,
                showAbsentConnectionScreen = true
            )
        }
    }

    private fun setDate(date: LocalDate) {
        state = state.copy(selectedDate = date)
        savedStateHandle[STATE_KEY_QUERY] = date
    }

    private fun List<Intervention>.groupByCurrentDisplayMode(): Map<String, List<WorkActivity>> {
        return when (state.displayMode) {
            DisplayMode.DAILY -> {
                this
                    .run {
                        if (getShouldShowGaps()) {
                            calculateGaps(
                                getStartTime(),
                                getEndTime(),
                                date = state.selectedDate
                            )
                        } else {
                            this
                        }
                    }
                    .groupBy { it.start.hour }
                    .mapKeys { "${it.key.toString().padStart(2, '0')}:00" }
                    .toList()
                    .sortedBy { (_, interventions) -> interventions.firstOrNull()?.date }
                    .toMap()
            }

            DisplayMode.WEEKLY -> {
                this
                    .removeGaps()
                    .groupBy { it.date }
                    .mapKeys {
                        "${
                            it.key.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                .replaceFirstChar { char -> char.uppercase() }
                        } ${it.key.dayOfMonth}/${it.key.monthValue}"
                    }
                    .toList()
                    .sortedBy { (_, interventions) -> interventions.firstOrNull()?.date }
                    .toMap()
            }
        }
    }

    private fun getShouldShowGaps(): Boolean {
        return sharedPreferences.getBoolean(SHOW_GAPS_SUGGESTIONS, true)
    }

    private fun getStartTime(): LocalTime {
        val savedStartTime = sharedPreferences.getString(WORK_START_TIME, "09:00")?.let {
            LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
        }
        return savedStartTime ?: LocalTime.of(9, 0)
    }

    private fun getEndTime(): LocalTime {
        val savedEndTime = sharedPreferences.getString(WORK_END_TIME, "18:00")?.let {
            LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
        }
        return savedEndTime ?: LocalTime.of(18, 0)
    }
}