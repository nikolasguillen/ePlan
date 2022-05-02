package com.example.eplan.presentation.ui.appointment

import androidx.lifecycle.SavedStateHandle
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailViewModel
@Inject
constructor(
    private val getToken: GetToken,
    private val validateTime: ValidateTime,
    savedStateHandle: SavedStateHandle
)
{
}