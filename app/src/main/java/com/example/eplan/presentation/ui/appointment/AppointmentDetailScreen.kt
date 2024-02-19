package com.example.eplan.presentation.ui.appointment

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.example.eplan.network.util.isConnectionAvailable
import com.example.eplan.presentation.ui.components.workActivity.WorkActivityDetail

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun AppointmentDetailScreen(
    viewModel: AppointmentDetailViewModel,
    topBarTitleResID: Int,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {
    WorkActivityDetail(
        viewModel = viewModel,
        topBarTitleResID = topBarTitleResID,
        onBackPressed = onBackPressed,
        onSaveAndClose = onSavePressed,
        onDeletePressed = onDeletePressed,
        shouldShowMissingConnectionWarning = isConnectionAvailable(LocalContext.current).not() && viewModel.appointment.value == null
    )
}
