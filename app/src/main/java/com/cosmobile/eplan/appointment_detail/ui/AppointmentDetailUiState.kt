package com.cosmobile.eplan.appointment_detail.ui

import com.cosmobile.eplan.appointment_detail.domain.model.Periodicity
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.presentation.ui.ActivitySelectorUiState
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailUiState

data class AppointmentDetailUiState(
    override val workActivity: Appointment? = null,
    override val activitySelectorUiState: ActivitySelectorUiState = ActivitySelectorUiState(),
    override val showAbsentConnectionScreen: Boolean = false,
    override val enableDeletion: Boolean = false,
    val initialAppointmentState: Appointment? = null,
    val descriptionSuggestions: List<String> = emptyList(),
    val usersSelectorUiState: UsersSelectorUiState? = null,
    val periodicityList: List<Periodicity> = listOf(
        Periodicity.None,
        Periodicity.Daily,
        Periodicity.Weekly,
        Periodicity.Biweekly,
        Periodicity.Monthly,
        Periodicity.Bimonthly
    ),
) : WorkActivityDetailUiState {
    override fun hasChanged(): Boolean {
        return initialAppointmentState != workActivity
    }
}
