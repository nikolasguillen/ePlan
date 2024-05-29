package com.cosmobile.eplan.appointment_detail.ui

data class UsersSelectorUiState(
    val invitedPeople: List<UserUiState> = emptyList(),
    val searchQuery: String = ""
)
