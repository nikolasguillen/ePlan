package com.cosmobile.eplan.appointments_list.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.appointments_list.domain.use_cases.DayChangeAppointment
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.DayChangeEvent
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnToggleCalendarState
import com.cosmobile.eplan.appointments_list.ui.AppointmentListEvent.OnToggleListState
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.core.domain.use_cases.ClearUserData
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.presentation.BaseApplication
import com.cosmobile.eplan.core.presentation.EplanViewModel
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.NetworkUtils
import com.cosmobile.eplan.interventions_list.ui.DisplayMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

const val STATE_KEY_QUERY = "appointment.state.query.key"

@HiltViewModel
class AppointmentListViewModel
@Inject
constructor(
    getToken: GetToken,
    private val context: BaseApplication,
    private val dayChangeAppointment: DayChangeAppointment,
    private val clearUserData: ClearUserData,
    private val savedStateHandle: SavedStateHandle,
    private val networkUtils: NetworkUtils
) : EplanViewModel() {

    var state by mutableStateOf(AppointmentListUiState())
        private set

    private val _logoutChannel = Channel<Unit>()
    val logoutChannel = _logoutChannel.receiveAsFlow()

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onEvent(event: AppointmentListEvent) {
        when (event) {
            is DayChangeEvent -> {
                setDate(event.date ?: state.selectedDate)
                dayChange()
            }

            is OnToggleCalendarState -> {
                state = state.copy(isCalendarExpanded = !state.isCalendarExpanded)
            }

            is OnToggleListState -> {
                state = state.copy(isListCollapsed = !state.isListCollapsed)
            }

            AppointmentListEvent.OnToggleVisualizationMode -> {
                val newDisplayMode = when (state.displayMode) {
                    DisplayMode.DAILY -> DisplayMode.WEEKLY
                    DisplayMode.WEEKLY -> DisplayMode.DAILY
                }
                state = state.copy(displayMode = newDisplayMode)
                dayChange(forceRefresh = true)
            }

            AppointmentListEvent.OnIncreaseDate -> {
                when (state.displayMode) {
                    DisplayMode.DAILY -> setDate(state.selectedDate.plusDays(1))
                    DisplayMode.WEEKLY -> setDate(state.selectedDate.plusDays(7))
                }
                dayChange()
            }

            AppointmentListEvent.OnDecreaseDate -> {
                when (state.displayMode) {
                    DisplayMode.DAILY -> setDate(state.selectedDate.minusDays(1))
                    DisplayMode.WEEKLY -> setDate(state.selectedDate.minusDays(7))
                }
                dayChange()
            }
        }
    }

    private fun onCreation() {
        savedStateHandle.get<String>(com.cosmobile.eplan.interventions_list.ui.STATE_KEY_QUERY)
            ?.let { q ->
                setDate(LocalDate.parse(q))
            }
        dayChange()
    }

    private fun dayChange(forceRefresh: Boolean = false) {
        state = state.copy(isCalendarExpanded = false)

        if (forceRefresh.not() && state.displayMode == DisplayMode.WEEKLY && state.appointments.isNotEmpty()) {
            val currentStartDate =
                state.appointments.entries.firstOrNull()?.value?.firstOrNull()?.date?.with(
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

            dayChangeAppointment.execute(
                token = userToken,
                startDate = startDate,
                endDate = endDate
            )
                .onEach { dataState ->
                    state = state.copy(isRefreshing = dataState.loading)

                    dataState.data?.let { list ->
                        state = state.copy(
                            appointments = list.groupByCurrentVisualMode(),
                            showAbsentConnectionScreen = false
                        )
                    }

                    dataState.error?.let { error ->
                        Log.e(GENERIC_DEBUG_TAG, "dayChange (Appointment): $error")
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

    private fun List<Appointment>.groupByCurrentVisualMode(): Map<String, List<WorkActivity>> {
        return when (state.displayMode) {
            DisplayMode.DAILY -> {
                val test = this
                    .groupBy { it.start.hour }
                    .mapKeys { "${it.key}:00" }
                    .toList()
                    .sortedBy { (_, appointments) -> appointments.firstOrNull()?.date }
                    .toMap()
                test
            }

            DisplayMode.WEEKLY -> {
                this
                    .groupBy { it.date }
                    .mapKeys {
                        "${
                            it.key.dayOfWeek.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            ).replaceFirstChar { char -> char.uppercase() }
                        } ${it.key.dayOfMonth}/${it.key.monthValue}"
                    }
                    .toList()
                    .sortedBy { (_, appointments) -> appointments.firstOrNull()?.date }
                    .toMap()
            }
        }
    }
}