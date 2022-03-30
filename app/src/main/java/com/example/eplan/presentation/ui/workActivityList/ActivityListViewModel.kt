package com.example.eplan.presentation.ui.workActivityList

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.workActivityList.ActivityListEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.repository.WorkActivityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val repository: WorkActivityRepository,
    @Named("auth_token") private val userToken: String,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    val query = mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))

    init {
        savedStateHandle.get<String>(STATE_KEY_QUERY)
            ?.let { Log.d(TAG, "Query (init, savedStateHandle):$it") }

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }

        Log.d(TAG, "Query (init): ${query.value}")

        if (query.value != LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(DayChangeEvent)
        }
    }

    fun onTriggerEvent(event: ActivityListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is DayChangeEvent -> {
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

    private suspend fun restoreState() {
        resetActivitiesState()
        val results = repository.getDayActivities(
            userToken = userToken,
            query = query.value,
            context = context
        )
        workActivities.value = results
    }

    fun onQueryChanged(query: String) {
        setQuery(query = query)
    }


    private suspend fun dayChange() {
        resetActivitiesState()
        val result =
            repository.getDayActivities(
                userToken = userToken,
                query = query.value,
                context = context
            )
        workActivities.value = result
        workActivities.value.sortedBy { it.start }
    }

    private fun resetActivitiesState() {
        workActivities.value = listOf()
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
        Log.d(TAG, "Query (dentro savedStateHandle):${savedStateHandle.get<String>(STATE_KEY_QUERY)!!}")
    }
}