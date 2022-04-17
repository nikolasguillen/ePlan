package com.example.eplan.presentation.ui.appointmentList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.eplan.domain.model.Appointment
import com.example.eplan.interactors.GetToken
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class AppointmentListViewModel
@Inject
constructor(
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val appointment: MutableState<List<Appointment>> = mutableStateOf(listOf())

    private var userToken = USER_TOKEN

    val loading = mutableStateOf(false)

    val date = mutableStateOf("")
}