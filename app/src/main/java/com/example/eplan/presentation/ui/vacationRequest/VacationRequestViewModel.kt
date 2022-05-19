package com.example.eplan.presentation.ui.vacationRequest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VacationRequestViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val startDate: MutableState<LocalDate?> = mutableStateOf(null)
    val endDate: MutableState<LocalDate?> = mutableStateOf(null)

    init {

    }
}