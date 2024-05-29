package com.cosmobile.eplan.appointment_detail.ui

import com.cosmobile.eplan.appointment_detail.domain.model.User

data class UserUiState(
    val user: User? = null,
    val isSelected: Boolean = false
)
