package com.cosmobile.eplan.core.presentation.ui.components.workActivity

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailEvent
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailUiState
import com.cosmobile.eplan.core.presentation.ValidationEvent
import com.cosmobile.eplan.core.presentation.ValidationEvent.DeleteSuccess
import com.cosmobile.eplan.core.presentation.ValidationEvent.NoConnection
import com.cosmobile.eplan.core.presentation.ValidationEvent.RetrieveError
import com.cosmobile.eplan.core.presentation.ValidationEvent.SubmitError
import com.cosmobile.eplan.core.presentation.ValidationEvent.UpdateSuccess
import com.cosmobile.eplan.core.presentation.navigation.BottomNavBarItem
import com.cosmobile.eplan.core.presentation.navigation.BottomNavbarAction
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityDetailUiState
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnActivityQueryChanged
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnActivitySelected
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnToggleActivitySelectorVisibility
import com.cosmobile.eplan.core.presentation.ui.components.ActivitySelectorScreen
import com.cosmobile.eplan.core.presentation.ui.components.animations.SendAnimation
import com.cosmobile.eplan.core.presentation.ui.components.detailForms.AppointmentDetail
import com.cosmobile.eplan.core.presentation.ui.components.detailForms.InterventionDetail
import com.cosmobile.eplan.core.presentation.ui.components.placeholders.AbsentConnectionPlaceholder
import com.cosmobile.eplan.core.presentation.ui.components.placeholders.PlaceholderDetails
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.BottomActionBar
import com.cosmobile.eplan.core.util.UiText
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailEvent
import com.cosmobile.eplan.intervention_detail.ui.detail.InterventionDetailUiState
import kotlinx.coroutines.flow.Flow

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun WorkActivityDetail(
    state: WorkActivityDetailUiState,
    onFormEvent: (WorkActivityFormEvent) -> Unit,
    onEvent: (WorkActivityDetailEvent) -> Unit,
    validationEvents: Flow<ValidationEvent>,
    topBarTitle: UiText,
    retrieving: Boolean,
    sending: Boolean,
    onBackPressed: () -> Unit,
    onSaveAndClose: () -> Unit,
    onSaveAndContinue: (() -> Unit)? = null,
    onDeletePressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val showBackDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is UpdateSuccess -> {
                    // TODO per ora non mostro nulla, ma in futuro potrei mostrare un messaggio di successo
                }

                is DeleteSuccess -> onBackPressed()

                is SubmitError -> {
                    snackBarHostState.showSnackbar(message = event.error.asString(context))
                }

                is RetrieveError -> {
                    snackBarHostState.showSnackbar(message = "${event.error.asString(context)}\nTorno indietro...")
                    onBackPressed()
                }

                is NoConnection -> {
                    snackBarHostState.showSnackbar(message = context.getString(R.string.connessione_assente))
                }
            }
        }
    }

    Crossfade(
        targetState = state.activitySelectorUiState.isVisible,
        label = "FormDetail"
    ) { showSearchScreen ->
        if (showSearchScreen) {
            ActivitySelectorScreen(
                activities = state.activitySelectorUiState.activities,
                selectedActivityId = state.workActivity?.activityId,
                onActivitySelected = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onFormEvent(OnActivitySelected(it))
                },
                searchQuery = state.activitySelectorUiState.searchQuery,
                onQueryChange = { query ->
                    onFormEvent(OnActivityQueryChanged(query))
                },
                onBackPressed = { onFormEvent(OnToggleActivitySelectorVisibility) }
            )
        } else {
            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        title = { Text(text = topBarTitle.asString(context)) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (state.hasChanged()) {
                                        showBackDialog.value = true
                                    } else {
                                        onBackPressed()
                                        keyboardController?.hide()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.indietro)
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { onDeletePressed() },
                                enabled = state.enableDeletion,
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
                            actions = when (state) {
                                is InterventionDetailUiState -> {
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
                                }

                                is AppointmentDetailUiState -> {
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

                                else -> emptyList()
                            }
                        )
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                modifier = Modifier.imePadding()
            ) { paddingValues ->
                BackHandler(enabled = true) {
                    if (state.hasChanged()) {
                        showBackDialog.value = true
                    } else {
                        onBackPressed()
                        keyboardController?.hide()
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                        .systemBarsPadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (retrieving) {
                        PlaceholderDetails()
                    } else {
                        when (state) {
                            is InterventionDetailUiState -> {
                                InterventionDetail(
                                    state = state,
                                    onFormEvent = onFormEvent
                                )
                            }

                            is AppointmentDetailUiState -> {
                                AppointmentDetail(
                                    state = state,
                                    onFormEvent = onFormEvent
                                )
                            }
                        }
                    }
                }

                if (state.showAbsentConnectionScreen) {
                    AbsentConnectionPlaceholder(
                        onRetry = {
                            when (state) {
                                is InterventionDetailUiState -> {
                                    onEvent(InterventionDetailEvent.RefreshInterventionEvent)
                                }

                                is AppointmentDetailUiState -> {
                                    onEvent(AppointmentDetailEvent.RefreshAppointmentEvent)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable(
                                onClick = {},
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
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