package com.example.eplan.presentation.ui.workActivity

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.workActivityDetail.GetById
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named


const val STATE_KEY_ACTIVITY = "state.key.workActivityId"

@HiltViewModel
class ActivityDetailViewModel
@Inject
constructor(
    private val getById: GetById,
    private val repository: WorkActivityRepository,
    @Named("auth_token") private val userToken: String,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val onLoad = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val query = mutableStateOf("")

    private var initialState: MutableState<WorkActivity?> = mutableStateOf(null)
    var workActivity: MutableState<WorkActivity?> = mutableStateOf(null)
        private set

    fun updateTitle(title: String) {
        workActivity.value = workActivity.value?.copy(title = title)
    }

    fun updateDescription(description: String) {
        workActivity.value = workActivity.value?.copy(description = description)
    }

    fun updateDate(date: LocalDate) {
        workActivity.value = workActivity.value?.copy(date = date)
    }

    fun updateStart(time: LocalTime) {
        workActivity.value = workActivity.value?.copy(start = time)
    }

    fun updateEnd(time: LocalTime) {
        workActivity.value = workActivity.value?.copy(end = time)
    }

    fun updateMovingTime(time: String) {
        workActivity.value = workActivity.value?.copy(movingTime = time)
    }

    fun updateKm(km: String) {
        workActivity.value = workActivity.value?.copy(km = km)
    }

    fun checkChanges(): Boolean {
        return initialState.value != workActivity.value
    }

    init {
        savedStateHandle.get<String>(STATE_KEY_ACTIVITY)?.let { workActivityId ->
            onTriggerEvent(GetActivityEvent(workActivityId))
        }
    }

    fun onTriggerEvent(event: ActivityDetailEvent) {

        viewModelScope.launch {
            try {
                when (event) {
                    is GetActivityEvent -> {
                        setQuery(event.id)
                        getActivity()
                    }
                    is UpdateActivityEvent -> {
                        updateActivity()
                    }
                    is DeleteActivityEvent -> {
                        deleteActivity()
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityDetailViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }

    }

    private fun getActivity() {
        Log.d(TAG, "get by Id (viewModel): query: ${query.value}")
        resetActivity()

        getById.execute(token = userToken, id = query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { newActivity ->
                Log.d(TAG, "dentro il datastate: $newActivity")
                initialState.value = newActivity
                workActivity.value = newActivity
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getActivity: $error")
                // TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun updateActivity() {
        workActivity.value?.let {
            repository.updateWorkActivity(
                userToken = userToken,
                workActivity = it
            )
        }
        resetActivity()
    }

    private suspend fun deleteActivity() {
        workActivity.value?.let { repository.deleteWorkActivity(userToken = userToken, id = it.id) }
        resetActivity()
        savedStateHandle.remove<String>(STATE_KEY_ACTIVITY)
    }

    private fun resetActivity() {
        workActivity.value = null
        initialState.value = null
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_ACTIVITY, query)
    }
}