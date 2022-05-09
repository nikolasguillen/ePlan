package com.example.eplan.presentation.ui.intervention

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Intervention
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.interventionDetail.GetInterventionById
import com.example.eplan.interactors.interventionDetail.SubmitIntervention
import com.example.eplan.interactors.workActivityDetail.ValidateActivityId
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionDetailEvent.*
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.fromDateToLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

const val STATE_KEY_INTERVENTION_ID = "intervention.state.interventionId.key"

@HiltViewModel
class InterventionDetailViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getInterventionById: GetInterventionById,
    private val submitIntervention: SubmitIntervention,
    private val validateActivityId: ValidateActivityId,
    private val validateDescription: ValidateDescription,
    private val validateTime: ValidateTime,
    private val savedStateHandle: SavedStateHandle
) : WorkActivityDetailViewModel() {

    private var initialState: MutableState<Intervention?> = mutableStateOf(null)
    var intervention: MutableState<Intervention?> = mutableStateOf(null)
        private set

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onTriggerEvent(event: InterventionDetailEvent) {
        when (event) {
            is GetInterventionEvent -> {
                changeQuery(event.id)
                getIntervention()
            }
            is UpdateInterventionEvent -> {
                updateIntervention()
            }
            is DeleteInterventionEvent -> {
                deleteIntervention()
            }
        }
    }

    private fun onCreation() {
        val id = savedStateHandle.get<String?>("activityId")
        val date = savedStateHandle.get<String>("date")
        val start = savedStateHandle.get<String?>("start")
        val end = savedStateHandle.get<String?>("end")

        if (id == null) {
            if (start == null && end == null) {
                createInterventionManually(date = LocalDate.parse(date))
            } else {
                createRecordedIntervention(
                    date = LocalDate.parse(date),
                    start = LocalTime.parse(start),
                    end = LocalTime.parse(end)
                )
            }
        } else {
            onTriggerEvent(GetInterventionEvent(id = id))
        }
    }

    private fun createInterventionManually(date: LocalDate) {
        intervention.value = Intervention(date = date)
        initialState.value = Intervention(date = date)
    }

    private fun createRecordedIntervention(date: LocalDate, start: LocalTime, end: LocalTime) {
        intervention.value = Intervention(date = date, start = start, end = end)
        initialState.value = Intervention(date = date, start = start, end = end)
    }

    fun onFormEvent(event: InterventionFormEvent) {
        when (event) {
            is TitleChanged -> {
                intervention.value = intervention.value?.copy(title = event.title)
            }
            is DescriptionChanged -> {
                intervention.value = intervention.value?.copy(description = event.description)
            }
            is DateChanged -> {
                intervention.value =
                    intervention.value?.copy(date = fromDateToLocalDate(event.date))
            }
            is StartChanged -> {
                intervention.value = intervention.value?.copy(start = event.time)
            }
            is EndChanged -> {
                intervention.value = intervention.value?.copy(end = event.time)
            }
            is MovingTimeChanged -> {
                intervention.value = intervention.value?.copy(movingTime = event.time)
            }
            is KmChanged -> {
                intervention.value = intervention.value?.copy(km = event.km)
            }
            is Submit -> {
                submitData()
            }
        }
    }

    override fun checkChanges(): Boolean {
        return intervention.value != initialState.value
    }

    private fun submitData() {
        intervention.value?.let {
            // TODO quando arrivo qua devo aver già popolato la mappa di attività (id, nome)
            val activityIdResult = validateActivityId.execute(it.activityId, listOf())
            val descriptionResult = validateDescription.execute(it.description)
            val timeResult = validateTime.execute(it.start, it.end)

            val hasErrors = listOf(
                activityIdResult,
                descriptionResult,
                timeResult
            ).any { result -> !result.successful }

            if (hasErrors) {
                intervention.value = intervention.value?.copy(
                    activityIdError = activityIdResult.errorMessage,
                    descriptionError = descriptionResult.errorMessage,
                    timeError = timeResult.errorMessage
                )
                return
            }
            onTriggerEvent(UpdateInterventionEvent)
        }
    }

    private fun getIntervention() {
        getInterventionById.execute(token = userToken, id = query).onEach { dataState ->
            retrieving = dataState.loading

            dataState.data?.let { newIntervention ->
                initialState.value = newIntervention
                intervention.value = newIntervention
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getIntervention: $error")
                validationEventChannel.send(ValidationEvent.RetrieveError(error = error))
            }
        }.launchIn(viewModelScope)
    }

    private fun updateIntervention() {
        intervention.value?.let {
            submitIntervention.execute(token = userToken, intervention = it)
                .onEach { dataState ->
                    sending = dataState.loading

                    dataState.data?.let { result ->
                        if (result) {
                            validationEventChannel.send(ValidationEvent.UpdateSuccess)
                        }
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "updateIntervention: $error")
                        validationEventChannel.send(ValidationEvent.SubmitError(error = error))
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun deleteIntervention() {
        intervention.value?.let {
            //TODO
        }
        savedStateHandle.remove<String>(STATE_KEY_INTERVENTION_ID)
    }

    override fun changeQuery(query: String) {
        this.query = query
        savedStateHandle.set(STATE_KEY_INTERVENTION_ID, query)
    }
}