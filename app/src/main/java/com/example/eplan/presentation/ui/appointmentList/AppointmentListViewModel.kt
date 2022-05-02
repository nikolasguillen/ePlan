package com.example.eplan.presentation.ui.appointmentList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.eplan.domain.model.Appointment
import com.example.eplan.interactors.GetToken
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

const val STATE_KEY_QUERY = "activity.state.query.key"

@HiltViewModel
class AppointmentListViewModel
@Inject
constructor(
    private val getToken: GetToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var userToken = USER_TOKEN
    val appointments = mutableStateListOf<Appointment>()
    val loading = mutableStateOf(false)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val date = mutableStateOf(LocalDate.now().toString())
}