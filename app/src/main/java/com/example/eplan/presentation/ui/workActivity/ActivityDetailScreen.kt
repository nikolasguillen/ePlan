package com.example.eplan.presentation.ui.workActivity

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.ui.components.BottomSaveBar
import com.example.eplan.presentation.ui.components.PlaceholderDetails
import com.example.eplan.presentation.ui.components.WorkActivityDetail
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.GetActivityEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    activityId: String,
    date: LocalDate,
    start: LocalTime? = null,
    end: LocalTime? = null,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    if (activityId != "null") {
        // Evita di rifare la chiamata API ad ogni recomposition
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(GetActivityEvent(activityId))
        }
    } else {
        if (start != null && end != null) {
            viewModel.createRecordedActivity(date = date, start = start, end = end)
        } else {
            viewModel.createManualActivity(date = date)
        }
    }

    val loading = viewModel.loading.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val backDialog = remember { mutableStateOf(false) }

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
                            if (!loading) {
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
                if (!loading) {
                    onSavePressed()
                    if (viewModel.error != null) {
                        scope.launch {
                            snackBarHostState.showSnackbar(message = viewModel.error!!)
                        }
                    } else run {
                        onBackPressed()
                    }
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

            if (loading) {
                PlaceholderDetails()
            } else {
                if (viewModel.error != null) {
                    LaunchedEffect(key1 = true) {
                        scope.launch {
                            snackBarHostState.showSnackbar("Intervento non trovato, errore: ${viewModel.error} \n Ritorno alla schermata precedente...")
                        }
                        delay(3000)
                        onBackPressed()
                    }
                }
                WorkActivityDetail(
                    viewModel = viewModel,
                    topPadding = paddingValues.calculateTopPadding(),
                    bottomPadding = paddingValues.calculateBottomPadding()
                )

            }
        }
    )


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

