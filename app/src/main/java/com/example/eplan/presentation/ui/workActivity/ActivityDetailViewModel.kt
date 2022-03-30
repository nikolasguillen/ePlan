package com.example.eplan.presentation.ui.workActivity

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.*
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named


const val STATE_KEY_ACTIVITY = "state.key.workActivityId"
@HiltViewModel
class ActivityDetailViewModel
@Inject
constructor(
    private val repository: WorkActivityRepository,
    @Named("auth_token") private val userToken: String,
    @ApplicationContext private val context: Context,
    private val state: SavedStateHandle
) : ViewModel() {

    val workActivity: MutableState<WorkActivity?> = mutableStateOf(null)

    val onLoad = mutableStateOf(false)

    init {
        state.get<String>(STATE_KEY_ACTIVITY)?.let { workActivityId ->
            onTriggerEvent(GetActivityEvent(workActivityId))
        }
    }

    fun onTriggerEvent(event: ActivityDetailEvent) {

        viewModelScope.launch {
            try {
                when (event) {
                    is GetActivityEvent -> {
                        getActivity(event.id)
                    }
                    is UpdateActivityEvent -> {
                        updateActivity(event.workActivity)
                    }
                    is DeleteActivityEvent -> {
                        deleteActivity(event.id)
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityDetailViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }

    }

    private suspend fun getActivity(id: String) {
        resetActivity()
        val result = repository.getActivityById(userToken = userToken, activityId = id)

        state.set(STATE_KEY_ACTIVITY, id)

        workActivity.value = result
    }

    private suspend fun updateActivity(workActivity: WorkActivity) {
        repository.updateWorkActivity(userToken = userToken, workActivity = workActivity)
        resetActivity()
    }

    private suspend fun deleteActivity(id: String) {
        repository.deleteWorkActivity(userToken = userToken, id = id)
        resetActivity()
        state.remove<String>(STATE_KEY_ACTIVITY)
    }

    private fun resetActivity() {
        workActivity.value = null
    }

}