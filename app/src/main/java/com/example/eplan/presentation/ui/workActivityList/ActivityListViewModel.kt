package com.example.eplan.presentation.ui.workActivityList

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.di.NetworkModule
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.workActivityList.DayChange
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.DayChangeEvent
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.RestoreStateEvent
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_QUERY = "activity.state.query.key"
const val STATE_KEY_TOKEN = "activity.state.token.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val dayChange: DayChange,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    val loading = mutableStateOf(false)

    val date = mutableStateOf(LocalDate.now().toString())

    init {

        savedStateHandle.get<String>(STATE_KEY_TOKEN)?.let { restoredToken ->
            NetworkModule.setToken(restoredToken)
        }

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setDate(q)
        }

        if (date.value != LocalDate.now().toString()) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(DayChangeEvent(date.value))
        }
    }

    fun onTriggerEvent(event: ActivityListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is DayChangeEvent -> {
                        setDate(event.date)
                        dayChange()
                    }
                    RestoreStateEvent -> {
                        dayChange()
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityListViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }


    private fun dayChange() {
        resetActivitiesState()

        dayChange.execute(token = NetworkModule.getToken(), query = date.value).onEach { dataState ->
            loading.value = dataState.loading

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
        if (savedStateHandle.get<String>(STATE_KEY_TOKEN).isNullOrBlank()) {
            saveToken()
        }
        this.date.value = date
        savedStateHandle.set(STATE_KEY_QUERY, date)
    }

    private fun saveToken() {
        savedStateHandle.set(STATE_KEY_TOKEN, NetworkModule.getToken(header = false))
    }
}