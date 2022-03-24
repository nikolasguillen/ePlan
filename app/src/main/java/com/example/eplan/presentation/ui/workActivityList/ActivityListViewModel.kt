package com.example.eplan.presentation.ui.workActivityList

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel
@Inject
constructor(
    private val repository: WorkActivityRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    val workActivities: MutableState<List<WorkActivity>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            val result = repository.getMonthActivities(userToken = "", month = 2, context = context)
            workActivities.value = result
        }
    }
}