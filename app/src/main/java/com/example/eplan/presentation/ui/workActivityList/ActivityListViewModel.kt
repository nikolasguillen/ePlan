package com.example.eplan.presentation.ui.workActivityList

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.workActivityList.DayChangeWorkActivity
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.DayChangeEvent
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val dayChangeWorkActivity: DayChangeWorkActivity,
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var userToken = USER_TOKEN
    val workActivities = mutableStateListOf<WorkActivity>()
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
        }
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->
            loading.value = dataState.loading
            _isRefreshing.value = dataState.loading

            dataState.data?.let { token ->
                userToken += token
                savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
                    setDate(q)
                }
                dayChange()
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun dayChange() {
        dayChangeWorkActivity.execute(token = userToken, query = date.value).onEach { dataState ->
            loading.value = dataState.loading
            _isRefreshing.value = dataState.loading

            dataState.data?.let { list ->
                workActivities.clear()
                workActivities.addAll(list)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "dayChange (workActivity): $error")
                // TODO Gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun setDate(date: String) {
        this.date.value = date
        savedStateHandle.set(STATE_KEY_QUERY, date)
    }
}