package com.example.eplan.presentation.ui.workActivityList

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.workActivityList.DayChange
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.DayChangeEvent
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.RestoreStateEvent
import com.example.eplan.presentation.util.TAG
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val dayChange: DayChange,
    @Named("auth_token") private val userToken: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    val loading = mutableStateOf(false)

    val date = mutableStateOf(LocalDate.now().toString())

    init {

        Log.d(TAG, "viewModel init: ${date.value}")

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            Log.d(TAG, "savedStateHandle in init: $q")
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
                        Log.d(TAG, "token: $userToken")
                        setDate(event.date)
                        dayChange()
                    }
                    RestoreStateEvent -> {
                        restoreState()
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityListViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }


    private fun dayChange() {
        Log.d(TAG, "dayChange: query: ${date.value}")
        resetActivitiesState()

        dayChange.execute(token = userToken, query = date.value).onEach { dataState ->
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

    private fun restoreState() {
        Log.d(TAG, "restoreState: query: ${this.date.value}")
        resetActivitiesState()

        dayChange.execute(token = userToken, query = date.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                workActivities.value = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "restoreState: $error")
                // TODO gestire errori
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