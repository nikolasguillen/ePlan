package com.example.eplan.presentation.ui.components.workActivity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
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
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent
import com.example.eplan.presentation.ui.components.*
import com.example.eplan.presentation.ui.components.detailForms.AppointmentDetail
import com.example.eplan.presentation.ui.components.detailForms.InterventionDetail
import com.example.eplan.presentation.ui.components.placeholders.PlaceholderDetails
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent
import com.example.eplan.presentation.util.spacing

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
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
    val showBackDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var showActivitySearch by remember { mutableStateOf(false) }

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

    Crossfade(targetState = showActivitySearch) { showSearchScreen ->
        if (showSearchScreen) {
            when (viewModel) {
                is InterventionDetailViewModel -> {
                    ActivitySelectorScreen(
                        activities = viewModel.activitiesList.toList(),
                        selectedActivityId = viewModel.intervention.value?.activityId,
                        onActivitySelected = {
                            viewModel.onFormEvent(
                                InterventionFormEvent.ActivityIdChanged(
                                    it
                                )
                            )
                        },
                        searchQuery = viewModel.activitySearchQuery,
                        onQueryChange = { query -> viewModel.activitySearchQuery = query },
                        onBackPressed = { showActivitySearch = false })
                }
                is AppointmentDetailViewModel -> {
                    ActivitySelectorScreen(
                        activities = viewModel.activitiesList.toList(),
                        selectedActivityId = viewModel.appointment.value?.activityId,
                        onActivitySelected = {
                            viewModel.onFormEvent(
                                AppointmentFormEvent.ActivityIdChanged(
                                    it
                                )
                            )
                        },
                        searchQuery = viewModel.activitySearchQuery,
                        onQueryChange = { query -> viewModel.activitySearchQuery = query },
                        onBackPressed = { showActivitySearch = false })
                }
            }
        } else {
            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        title = { Text(text = stringResource(id = topBarTitleResID)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                if (viewModel.checkChanges()) {
                                    showBackDialog.value = true
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
                    AnimatedVisibility(
                        visible = !retrieving,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        BottomSingleActionBar(
                            item = BottomNavBarItems.Save,
                            onClick = {
                                if (!retrieving) {
                                    onSavePressed()
                                }
                            }
                        )
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { paddingValues ->
                BackHandler(enabled = true) {
                    if (viewModel.checkChanges()) {
                        showBackDialog.value = true
                    } else {
                        onBackPressed()
                        keyboardController?.hide()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(paddingValues)
                        .consumedWindowInsets(paddingValues)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    if (retrieving) {
                        PlaceholderDetails()
                    } else {
                        when (viewModel) {
                            is InterventionDetailViewModel -> {
                                InterventionDetail(
                                    viewModel = viewModel,
                                    onActivitySelectionClick = { showActivitySearch = true }
                                )
                            }
                            is AppointmentDetailViewModel -> {
                                AppointmentDetail(
                                    viewModel = viewModel,
                                    onActivitySelectionClick = { showActivitySearch = true })
                            }
                        }
                    }
                }

                if (sending) {
                    Dialog(onDismissRequest = {}) {
                        SendAnimation()
                    }
                }

                if (showBackDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            showBackDialog.value = false
                        },
                        title = { Text(text = stringResource(R.string.chiudi_senza_salvare)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showBackDialog.value = false
                                onBackPressed()
                            }
                            ) {
                                Text(text = stringResource(R.string.conferma))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showBackDialog.value = false
                            }
                            ) {
                                Text(text = stringResource(R.string.annulla))
                            }
                        }
                    )
                }
            }
        }
    }
}