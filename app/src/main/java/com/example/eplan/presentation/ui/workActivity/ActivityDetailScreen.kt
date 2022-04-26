package com.example.eplan.presentation.ui.workActivity

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.components.BottomSaveBar
import com.example.eplan.presentation.ui.components.PlaceholderDetails
import com.example.eplan.presentation.ui.components.WorkActivityDetail
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.GetActivityEvent
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

    if (activityId != "null") {
        // Evita di rifare la chiamata API ad ogni recomposition
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(GetActivityEvent(activityId))
        }
    } else {
        if (start != null && end != null) {
            val temp = WorkActivity(date = date, start = start, end = end)
//            viewModel.initialState.value = WorkActivity()
            viewModel.workActivity.value = WorkActivity()
            viewModel.updateDate(date = date)
            viewModel.updateStart(time = start)
            viewModel.updateEnd(time = end)
            viewModel.initialState.value = temp
        } else {
            val temp = WorkActivity(date = date)
            viewModel.createManualActivity(date = date)
        }
    }

    val workActivity = viewModel.workActivity

    val loading = viewModel.loading.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val backDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.errore_orario),
        Toast.LENGTH_SHORT
    )


    if (workActivity.value == null && viewModel.error != null) {
        Scaffold(
            topBar = {
                MediumTopAppBar(
                    title = { Text(text = stringResource(R.string.errore)) },
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
                    }
                )
            }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 100.dp)
            ) {
                Text(
                    text = "Attenzione",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Intervento non trovato",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "errore: ${viewModel.error}",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    } else {
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
//                        onSaveClick(
//                            saveAction = onSavePressed,
//                            saveCondition = {
////                                acceptableTimeInterval(
////                                    workActivity.value?.start,
////                                    workActivity.value?.end
////                                )
//                                            true
//                            },
//                            elseBranch = {
//                                toast.cancel()
//                                toast.setText(R.string.errore_orario)
//                                toast.show()
//                            }
//                        )
                        onSavePressed()
                        if (viewModel.error != null) {
                            scope.launch {
                                snackBarHostState.showSnackbar(message = viewModel.error!!)
                            }
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
                        WorkActivityDetail(
                            viewModel = viewModel,
                            topPadding = paddingValues.calculateTopPadding(),
                            bottomPadding = paddingValues.calculateBottomPadding()
                        )

                }
            }
        )
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

private fun onSaveClick(
    saveAction: () -> Unit,
    saveCondition: () -> Boolean,
    elseBranch: () -> Unit
): Boolean {
//    if (saveCondition()) {
//        saveAction()
//    } else {
//        elseBranch()
//    }
    saveAction()
    return true
}

