package com.example.eplan.presentation.ui.workActivity

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
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.ui.components.BottomSaveBar
import com.example.eplan.presentation.ui.components.PlaceholderDetails
import com.example.eplan.presentation.ui.components.SendAnimation
import com.example.eplan.presentation.ui.components.WorkActivityDetail
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val retrieving = viewModel.retrieving.value
    val sending = viewModel.sending.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val backDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ActivityDetailViewModel.ValidationEvent.UpdateSuccess -> {
                    onBackPressed()
                }
                is ActivityDetailViewModel.ValidationEvent.SubmitError -> {
                    snackBarHostState.showSnackbar(message = event.error)
                }
                is ActivityDetailViewModel.ValidationEvent.RetrieveError -> {
                    snackBarHostState.showSnackbar(message = event.error)
                    onBackPressed()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(R.string.intervento)) },
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
                        onClick = {
                            if (!retrieving) {
                                onDeletePressed()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = Color.Red,
                            contentDescription = stringResource(R.string.elimina_commessa)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomSaveBar(onClick = {
                if (!retrieving) {
                    onSavePressed()
                }
            })
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
                    WorkActivityDetail(
                        viewModel = viewModel
                    )
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

