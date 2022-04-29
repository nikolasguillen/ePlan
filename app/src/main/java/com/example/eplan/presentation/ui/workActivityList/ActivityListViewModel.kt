package com.example.eplan.presentation.ui.workActivityList

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.di.NetworkModule
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.workActivityList.DayChange
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.DayChangeEvent
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.RestoreStateEvent
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val dayChange: DayChange,
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    var isReady by mutableStateOf(false)
        private set
    private var userToken = USER_TOKEN
    val loading = mutableStateOf(false)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val date = mutableStateOf(LocalDate.now().toString())

    init {
        getToken()
    }

    fun onTriggerEvent(event: ActivityListEvent) {
        when (event) {
            is DayChangeEvent -> {
                setDate(event.date)
                dayChange()
            }
            is RestoreStateEvent -> {
                dayChange()
            }
        }
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->

            dataState.data?.let { token ->
                userToken += token
                savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
                    setDate(q)
                }
                isReady = true
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun dayChange() {
        resetActivitiesState()

        dayChange.execute(token = userToken, query = date.value).onEach { dataState ->
            loading.value = dataState.loading
            _isRefreshing.value = dataState.loading

            dataState.data?.let { list ->
                workActivities.value = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "dayChange: $error")
                // TODO "Gestire errori"
            }
        }.launchIn(viewModelScope)
    }

    private fun resetActivitiesState() {
        workActivities.value = listOf()
    }

    private fun setDate(date: String) {
        this.date.value = date
        savedStateHandle.set(STATE_KEY_QUERY, date)
    }
}