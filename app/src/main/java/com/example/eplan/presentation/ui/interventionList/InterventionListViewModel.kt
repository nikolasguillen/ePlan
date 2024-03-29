package com.example.eplan.presentation.ui.interventionList

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.Intervention
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.interventionList.DayChangeIntervention
import com.example.eplan.network.util.isConnectionAvailable
import com.example.eplan.presentation.BaseApplication
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.interventionList.InterventionListEvent.DayChangeEvent
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class InterventionListViewModel
@Inject
constructor(
    getToken: GetToken,
    private val context: BaseApplication,
    private val dayChangeIntervention: DayChangeIntervention,
    private val savedStateHandle: SavedStateHandle
) : EplanViewModel() {

    val interventions = mutableStateListOf<Intervention>()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val date = mutableStateOf(LocalDate.now().toString())
    var isConnectionAvailable by mutableStateOf(false)
        private set

    init {
        getToken(getToken = getToken, onTokenRetrieved = { onCreation() })
    }

    fun onTriggerEvent(event: InterventionListEvent) {
        when (event) {
            is DayChangeEvent -> {
                setDate(event.date)
                dayChange()
            }
        }
    }

    private fun onCreation() {
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setDate(q)
        }
        dayChange()
    }

    private fun dayChange() {
        isConnectionAvailable = isConnectionAvailable(context = context)

        if (isConnectionAvailable) {
            dayChangeIntervention.execute(token = userToken, query = date.value).onEach { dataState ->
                _isRefreshing.value = dataState.loading

                dataState.data?.let { list ->
                    interventions.clear()
                    interventions.addAll(list)
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "dayChange (workActivity): $error")
                    // TODO Gestire errori
                }
            }.launchIn(viewModelScope)
        } else {
            _isRefreshing.value = false
        }
    }

    private fun setDate(date: String) {
        this.date.value = date
        savedStateHandle[STATE_KEY_QUERY] = date
    }
}