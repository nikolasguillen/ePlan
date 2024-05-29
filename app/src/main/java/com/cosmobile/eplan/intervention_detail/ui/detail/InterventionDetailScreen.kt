package com.cosmobile.eplan.intervention_detail.ui.detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.cosmobile.eplan.core.presentation.ValidationEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailUiState
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import com.cosmobile.eplan.core.presentation.ui.components.workActivity.WorkActivityDetail
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun InterventionDetailsScreen(
    state: WorkActivityDetailUiState,
    onFormEvent: (WorkActivityFormEvent) -> Unit,
    onEvent: (WorkActivityDetailEvent) -> Unit,
    validationEvents: Flow<ValidationEvent>,
    retrieving: Boolean,
    sending: Boolean,
    topBarTitle: UiText,
    onBackPressed: () -> Unit,
    onSaveAndClosePressed: () -> Unit,
    onSaveAndContinuePressed: (() -> Unit)? = null,
    onDeletePressed: () -> Unit
) {
    WorkActivityDetail(
        state = state,
        onFormEvent = onFormEvent,
        onEvent = onEvent,
        topBarTitle = topBarTitle,
        onBackPressed = onBackPressed,
        onSaveAndClose = onSaveAndClosePressed,
        onSaveAndContinue = onSaveAndContinuePressed,
        onDeletePressed = onDeletePressed,
        validationEvents = validationEvents,
        retrieving = retrieving,
        sending = sending
    )
}

