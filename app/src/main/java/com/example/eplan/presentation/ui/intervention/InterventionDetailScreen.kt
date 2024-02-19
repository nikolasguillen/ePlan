package com.example.eplan.presentation.ui.intervention

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.example.eplan.network.util.isConnectionAvailable
import com.example.eplan.presentation.ui.components.workActivity.WorkActivityDetail

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun InterventionDetailsScreen(
    viewModel: InterventionDetailViewModel,
    topBarTitleResID: Int,
    onBackPressed: () -> Unit,
    onSaveAndClosePressed: () -> Unit,
    onSaveAndContinuePressed: () -> Unit = {},
    onDeletePressed: () -> Unit
) {
    WorkActivityDetail(
        viewModel = viewModel,
        topBarTitleResID = topBarTitleResID,
        onBackPressed = onBackPressed,
        onSaveAndClose = onSaveAndClosePressed,
        onSaveAndContinue = onSaveAndContinuePressed,
        onDeletePressed = onDeletePressed,
        shouldShowMissingConnectionWarning = isConnectionAvailable(LocalContext.current).not() && viewModel.intervention.value == null
    )
}

