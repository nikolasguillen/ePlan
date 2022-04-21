package com.example.eplan.presentation.ui.workActivity

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.di.NetworkModule
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.workActivityDetail.GetById
import com.example.eplan.interactors.workActivityDetail.UpdateActivity
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject


const val STATE_KEY_ACTIVITY = "activity.state.workActivityId.key"

@HiltViewModel
class ActivityDetailViewModel
@Inject
constructor(
    private val getById: GetById,
    private val updateActivity: UpdateActivity,
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val onLoad = mutableStateOf(false)

    val loading = mutableStateOf(false)

    private val query = mutableStateOf("")

    var error: String? = null
        private set

    private var userToken = USER_TOKEN

    var initialState: MutableState<WorkActivity?> = mutableStateOf(null)
        private set
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
        getToken()
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

    private fun getToken() {
        getToken.execute().onEach { dataState ->

            dataState.data?.let { token ->
                userToken += token
                savedStateHandle.get<String>(STATE_KEY_ACTIVITY)?.let { workActivityId ->
                    onTriggerEvent(GetActivityEvent(workActivityId))
                }
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun getActivity() {
        resetActivity()

        if (userToken != USER_TOKEN) {
            getById.execute(token = userToken, id = query.value).onEach { dataState ->
                loading.value = dataState.loading

                dataState.data?.let { newActivity ->
                    initialState.value = newActivity
                    workActivity.value = newActivity
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getActivity: $error")
                    this.error = error
                    // TODO gestire testo errore
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateActivity() {
        workActivity.value?.let {

            updateActivity.execute(token = userToken, workActivity = it)
                .onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { result ->
                        if (result) resetActivity()
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "updateActivity: $error")
                        //TODO gestire errori
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun deleteActivity() {
        workActivity.value?.let {
            //TODO
        }
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