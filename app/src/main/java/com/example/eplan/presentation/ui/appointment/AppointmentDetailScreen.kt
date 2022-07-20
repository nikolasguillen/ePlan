package com.example.eplan.presentation.ui.appointment

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
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
        onSavePressed = onSavePressed,
        onDeletePressed = onDeletePressed
    )
}
