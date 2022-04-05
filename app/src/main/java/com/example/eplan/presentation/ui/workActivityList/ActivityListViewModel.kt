package com.example.eplan.presentation.ui.workActivityList

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.getDateFormatter
import com.example.eplan.interactors.workActivityList.DayChange
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.DayChangeEvent
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.RestoreStateEvent
import com.example.eplan.presentation.util.TAG
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val dayChange: DayChange,
    private val repository: WorkActivityRepository,
    @Named("auth_token") private val userToken: String,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    val loading = mutableStateOf(false)

    val query = mutableStateOf(LocalDate.now().toString())

    init {

        Log.d(TAG, query.value)

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }

//        if (query.value != LocalDate.now().toString()) {
//            onTriggerEvent(RestoreStateEvent)
//        } else {
            onTriggerEvent(DayChangeEvent)
//        }
    }

    fun onTriggerEvent(event: ActivityListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is DayChangeEvent -> {
                        Log.d(TAG, "token: $userToken")
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

    fun onQueryChanged(query: String) {
        setQuery(query = query)
    }


    private fun dayChange() {
        Log.d(TAG, "dayChange: query: ${query.value}")
        resetActivitiesState()

        dayChange.execute(token = userToken, query = query.value).onEach { dataState ->
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

    private suspend fun restoreState() {
        resetActivitiesState()
        val results = repository.getDayActivities(
            userToken = userToken,
            query = query.value,
            context = context
        )
        workActivities.value = results
    }

    private fun resetActivitiesState() {
        workActivities.value = listOf()
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}