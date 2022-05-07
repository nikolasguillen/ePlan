package com.example.eplan.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun WorkActivityDetail(
    viewModel: WorkActivityDetailViewModel,
    topBarTitleResID: Int,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    val retrieving = viewModel.retrieving
    val sending = viewModel.sending
    val keyboardController = LocalSoftwareKeyboardController.current
    val backDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.UpdateSuccess -> {
                    onBackPressed()
                }
                is ValidationEvent.SubmitError -> {
                    snackBarHostState.showSnackbar(message = event.error)
                }
                is ValidationEvent.RetrieveError -> {
                    snackBarHostState.showSnackbar(message = "${event.error}\nTorno indietro...")
                    onBackPressed()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(id = topBarTitleResID)) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.checkChanges()) {
                            backDialog.value = true
                        } else {
                            onBackPressed()
                            keyboardController?.hide()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onDeletePressed() },
                        enabled = !retrieving
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = if (retrieving) MaterialTheme.colorScheme.tertiary else Color.Red,
                            contentDescription = stringResource(R.string.elimina_commessa)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomSaveBar(
                onClick = {
                    if (!retrieving) {
                        onSavePressed()
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        content = { paddingValues ->
            BackHandler(enabled = true) {
                if (viewModel.checkChanges()) {
                    backDialog.value = true
                } else {
                    onBackPressed()
                    keyboardController?.hide()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                if (retrieving) {
                    PlaceholderDetails()
                } else {
                    when (viewModel) {
                        is InterventionDetailViewModel -> {
                            InterventionDetail(viewModel = viewModel)
                        }
                        is AppointmentDetailViewModel -> {
                            AppointmentDetail(viewModel = viewModel)
                        }
                    }
                }
            }

            if (sending) {
                Dialog(onDismissRequest = {}) {
                    SendAnimation()
                }
            }

            if (backDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        backDialog.value = false
                    },
                    title = { Text(text = stringResource(R.string.chiudi_senza_salvare)) },
                    confirmButton = {
                        TextButton(onClick = {
                            backDialog.value = false
                            onBackPressed()
                        }
                        ) {
                            Text(text = stringResource(R.string.conferma))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            backDialog.value = false
                        }
                        ) {
                            Text(text = stringResource(R.string.annulla))
                        }
                    }
                )
            }
        }
    )
}