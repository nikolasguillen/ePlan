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
    @Named("auth_token") private val userToken: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    val loading = mutableStateOf(false)

    val date = mutableStateOf(LocalDate.now().toString())

    init {

        Log.d(TAG, "viewModel init: ${date.value}")

        savedStateHandle.get<String>(STATE_KEY_TOKEN)?.let { t ->

            // TODO non so come aggiornare il token dopo la process death
            // TODO devo capire come rimandarti al login, e' la cosa piu' giusta da fare
        }

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            Log.d(TAG, "savedStateHandle -> query in init: $q")
            setDate(q)
        }

        if (date.value != LocalDate.now().toString()) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            savedStateHandle.set(STATE_KEY_TOKEN, userToken)
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