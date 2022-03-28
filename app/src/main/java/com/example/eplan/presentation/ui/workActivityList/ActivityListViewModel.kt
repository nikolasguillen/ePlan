package com.example.eplan.presentation.ui.workActivityList

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.Month
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val repository: WorkActivityRepository,
    @Named("auth_token") private val userToken: String,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            onTriggerEvent(
                ActivityListEvent.DayChangeEvent(
                    LocalDate.now().dayOfMonth,
                    LocalDate.now().monthValue,
                    LocalDate.now().year
                )
            )
        }
    }

    fun onTriggerEvent(event: ActivityListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is ActivityListEvent.DayChangeEvent -> {
                        dayChange(event.dayOfMonth, event.month, event.year)
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityListViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }


    private suspend fun dayChange(dayOfMonth: Int, month: Int, year: Int) {
        resetActivitiesState()
        val result =
            repository.getDayActivities(
                userToken = userToken,
                dayOfMonth = dayOfMonth,
                month = month,
                year = year,
                context = context
            )
        workActivities.value = result
        workActivities.value.sortedBy { it.start }
    }

    private fun resetActivitiesState() {
        workActivities.value = listOf()
    }
}