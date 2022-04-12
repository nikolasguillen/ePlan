package com.example.eplan.presentation.ui.workActivity

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.components.BottomSaveBar
import com.example.eplan.presentation.ui.components.PlaceholderDetails
import com.example.eplan.presentation.ui.components.WorkActivityDetail
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.GetActivityEvent
import com.example.eplan.presentation.util.acceptableTimeInterval
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    activityId: String,
    date: LocalDate,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    val workActivity = viewModel.workActivity

    if (activityId != "null") {
        // Evita di rifare la chiamata API ad ogni recomposition
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(GetActivityEvent(activityId))
        }
    } else {
        val temp = WorkActivity(date = date)
        viewModel.initialState.value = temp
        workActivity.value = temp
    }

    val loading = viewModel.loading.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val backDialog = remember { mutableStateOf(false) }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.errore_orario),
        Toast.LENGTH_SHORT
    )


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
                    onSaveClick(
                        saveAction = onSavePressed,
                        saveCondition = {
                            acceptableTimeInterval(
                                workActivity.value?.start,
                                workActivity.value?.end
                            )
                        },
                        elseBranch = {
                            toast.cancel()
                            toast.setText(R.string.errore_orario)
                            toast.show()
                        }
                    )
                }
            })
        },
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
                workActivity.value?.let {
                    WorkActivityDetail(
                        viewModel = viewModel,
                        workActivity = it,
                        bottomPadding = paddingValues.calculateBottomPadding()
                    )
                }
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

private fun onSaveClick(
    saveAction: () -> Unit,
    saveCondition: () -> Boolean,
    elseBranch: () -> Unit
) {
    if (saveCondition()) {
        saveAction()
    } else {
        elseBranch()
    }
}