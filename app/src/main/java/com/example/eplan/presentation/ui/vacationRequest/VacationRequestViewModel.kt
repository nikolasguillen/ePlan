package com.example.eplan.presentation.ui.vacationRequest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.example.eplan.presentation.ui.EplanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VacationRequestViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle
) : EplanViewModel() {

    var singleDayVacancy by mutableStateOf(true)
    val singleDate = mutableStateOf(LocalDate.now())
    val startDate = mutableStateOf(LocalDate.now())
    val endDate = mutableStateOf(LocalDate.now())

    init {

    }
}