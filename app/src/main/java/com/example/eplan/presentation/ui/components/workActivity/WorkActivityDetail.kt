package com.example.eplan.presentation.ui.components.workActivity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.navigation.BottomNavBarItem
import com.example.eplan.presentation.navigation.BottomNavbarAction
import com.example.eplan.presentation.ui.ValidationEvent
import com.example.eplan.presentation.ui.WorkActivityDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent
import com.example.eplan.presentation.ui.components.ActivitySelectorScreen
import com.example.eplan.presentation.ui.components.animations.SendAnimation
import com.example.eplan.presentation.ui.components.detailForms.AppointmentDetail
import com.example.eplan.presentation.ui.components.detailForms.InterventionDetail
import com.example.eplan.presentation.ui.components.placeholders.PlaceholderDetails
import com.example.eplan.presentation.ui.components.uiElements.BottomActionBar
import com.example.eplan.presentation.ui.intervention.InterventionDetailViewModel
import com.example.eplan.presentation.ui.intervention.InterventionFormEvent
import com.example.eplan.presentation.util.spacing

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun WorkActivityDetail(
    viewModel: WorkActivityDetailViewModel,
    topBarTitleResID: Int,
    onBackPressed: () -> Unit,
    onSaveAndClose: () -> Unit,
    onSaveAndContinue: (() -> Unit)? = null,
    onDeletePressed: () -> Unit,
    shouldShowMissingConnectionWarning: Boolean
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
                    if (onSaveAndContinue != null) onBackPressed()
                }

                is ValidationEvent.SubmitError -> {
                    snackBarHostState.showSnackbar(message = event.error)
                }

                is ValidationEvent.RetrieveError -> {
                    snackBarHostState.showSnackbar(message = "${event.error}\nTorno indietro...")
                    onBackPressed()
                }

                is ValidationEvent.NoConnection -> {
                    snackBarHostState.showSnackbar(message = context.getString(R.string.connessione_assente))
                }
            }
        }
    }

    Crossfade(targetState = showActivitySearch, label = "FormDetail") { showSearchScreen ->
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
                                enabled = !retrieving && !sending && viewModel.isConnectionAvailable,
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
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
                        BottomActionBar(
                            actions = if (viewModel is InterventionDetailViewModel) {
                                listOf(
                                    BottomNavbarAction(
                                        item = BottomNavBarItem.SaveAndClose,
                                        onClick = {
                                            if (!retrieving) {
                                                onSaveAndClose()
                                            }
                                        }
                                    ),
                                    BottomNavbarAction(
                                        item = BottomNavBarItem.SaveAndContinue,
                                        onClick = {
                                            if (!retrieving) {
                                                onSaveAndContinue?.invoke()
                                            }
                                        }
                                    )
                                )
                            } else {
                                listOf(
                                    BottomNavbarAction(
                                        item = BottomNavBarItem.Save,
                                        onClick = {
                                            if (!retrieving) {
                                                onSaveAndClose()
                                            }
                                        }
                                    )
                                )
                            }
                        )
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                modifier = Modifier.imePadding()
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
                        .consumeWindowInsets(paddingValues)
                        .systemBarsPadding()
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

                if (shouldShowMissingConnectionWarning) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Image(
                            imageVector = Icons.Filled.WifiOff,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.8F
                                )
                            ),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        Text(
                            text = stringResource(id = R.string.connessione_assente),
                            style = MaterialTheme.typography.bodyLarge
                        )
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
                            TextButton(
                                onClick = {
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