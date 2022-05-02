package com.example.eplan.presentation.ui.workActivity

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.workActivityDetail.GetActivityById
import com.example.eplan.interactors.workActivityDetail.SubmitActivity
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import com.example.eplan.presentation.util.fromDateToLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

const val STATE_KEY_ACTIVITY = "activity.state.workActivityId.key"

@HiltViewModel
class ActivityDetailViewModel
@Inject
constructor(
    private val getActivityById: GetActivityById,
    private val submitActivity: SubmitActivity,
    private val getToken: GetToken,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var userToken = USER_TOKEN
    val retrieving = mutableStateOf(false)
    val sending = mutableStateOf(false)
    private var query = ""
    private var initialState: MutableState<WorkActivity?> = mutableStateOf(null)
    var workActivity: MutableState<WorkActivity?> = mutableStateOf(null)
        private set
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        getToken()

        val id = savedStateHandle.get<String?>("activityId")
        val date = savedStateHandle.get<String>("date")
        val start = savedStateHandle.get<String?>("start")
        val end = savedStateHandle.get<String?>("end")

        if (id == null) {
            if (start == null && end == null) {
                createManualActivity(date = LocalDate.parse(date))
            } else {
                createRecordedActivity(
                    date = LocalDate.parse(date),
                    start = LocalTime.parse(start),
                    end = LocalTime.parse(end)
                )
            }
        } else {
            onTriggerEvent(GetActivityEvent(id = id))
        }
    }

    fun onFormEvent(event: ActivityFormEvent) {
        when (event) {
            is ActivityFormEvent.TitleChanged -> {
                workActivity.value = workActivity.value?.copy(title = event.title)
            }
            is ActivityFormEvent.DescriptionChanged -> {
                workActivity.value = workActivity.value?.copy(description = event.description)
            }
            is ActivityFormEvent.DateChanged -> {
                workActivity.value =
                    workActivity.value?.copy(date = fromDateToLocalDate(event.date))
            }
            is ActivityFormEvent.StartChanged -> {
                workActivity.value = workActivity.value?.copy(start = event.time)
            }
            is ActivityFormEvent.EndChanged -> {
                workActivity.value = workActivity.value?.copy(end = event.time)
            }
            is ActivityFormEvent.MovingTimeChanged -> {
                workActivity.value = workActivity.value?.copy(movingTime = event.time)
            }
            is ActivityFormEvent.KmChanged -> {
                workActivity.value = workActivity.value?.copy(km = event.km)
            }
            is ActivityFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun createManualActivity(date: LocalDate) {
        workActivity.value = WorkActivity(date = date)
        initialState.value = WorkActivity(date = date)
    }

    private fun createRecordedActivity(date: LocalDate, start: LocalTime, end: LocalTime) {
        workActivity.value = WorkActivity(date = date, start = start, end = end)
        initialState.value = WorkActivity(date = date, start = start, end = end)
    }

    fun checkChanges(): Boolean {
        return workActivity.value != initialState.value
    }

    private fun submitData() {
        workActivity.value?.let {
            val descriptionResult = validateDescription.execute(it.description)
            val timeResult = validateTime.execute(it.start, it.end)

            val hasErrors = listOf(
                descriptionResult,
                timeResult
            ).any { result -> !result.successful }

            if (hasErrors) {
                workActivity.value = workActivity.value?.copy(
                    descriptionError = descriptionResult.errorMessage,
                    timeError = timeResult.errorMessage
                )
                return
            }
            onTriggerEvent(UpdateActivityEvent)
        }
    }

    fun onTriggerEvent(event: ActivityDetailEvent) {
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
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->
            retrieving.value = dataState.loading

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
        getActivityById.execute(token = userToken, id = query).onEach { dataState ->
            retrieving.value = dataState.loading

            dataState.data?.let { newActivity ->
                initialState.value = newActivity
                workActivity.value = newActivity
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getActivity: $error")
                validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
            }
        }.launchIn(viewModelScope)
    }

    private fun updateActivity() {
        workActivity.value?.let {
            submitActivity.execute(token = userToken, workActivity = it)
                .onEach { dataState ->
                    sending.value = dataState.loading

                    dataState.data?.let { result ->
                        if (result) {
                            validationEventChannel.send(ValidationEvent.UpdateSuccess)
                        }
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "updateActivity: $error")
                        validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun deleteActivity() {
        workActivity.value?.let {
            //TODO
        }
        savedStateHandle.remove<String>(STATE_KEY_ACTIVITY)
    }

    private fun setQuery(query: String) {
        this.query = query
        savedStateHandle.set(STATE_KEY_ACTIVITY, query)
    }

    sealed class ValidationEvent {
        object UpdateSuccess : ValidationEvent()
        data class SubmitError(val error: String) : ValidationEvent()
        data class RetrieveError(val error: String) : ValidationEvent()
    }
}