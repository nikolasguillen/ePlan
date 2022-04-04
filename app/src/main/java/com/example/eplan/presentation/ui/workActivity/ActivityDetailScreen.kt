package com.example.eplan.presentation.ui.workActivity

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.components.BottomSaveBar
import com.example.eplan.presentation.ui.components.CustomDateButton
import com.example.eplan.presentation.ui.components.CustomTimeButton
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.GetActivityEvent
import com.example.eplan.presentation.util.acceptableTimeInterval
import com.example.eplan.presentation.util.fromDateToLocalDate
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    activityId: String,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    // Evita di rifare la chiamata API ad ogni recomposition
    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(GetActivityEvent(activityId))
    }

    val workActivity = viewModel.workActivity.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val backDialog = remember { mutableStateOf(false) }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.errore_orario),
        Toast.LENGTH_SHORT
    )

    workActivity?.let {

        Scaffold(
            topBar = {
                MediumTopAppBar(
                    title = { Text(text = stringResource(R.string.attivita)) },
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
                        IconButton(onClick = {
                            onDeletePressed()
                        }) {
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
                    onSaveClick(
                        saveAction = onSavePressed,
                        saveCondition = { acceptableTimeInterval(it.start, it.end) },
                        elseBranch = {
                            toast.cancel()
                            toast.setText(R.string.errore_orario)
                            toast.show()
                        }
                    )
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = paddingValues.calculateBottomPadding()
                        )
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = it.title,
                        onValueChange = { viewModel.updateTitle(it) },
                        label = { Text(text = stringResource(R.string.attivita)) },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = it.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text(text = stringResource(R.string.descrizione)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomDateButton(
                            date = it.date,
                            onDateSelected = {
                                viewModel.updateDate(fromDateToLocalDate(it))
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomTimeButton(
                            time = it.start,
                            label = stringResource(R.string.ora_inizio),
                            onClick = { time ->
                                viewModel.updateStart(time)
                            }
                        )
                        CustomTimeButton(
                            time = it.end,
                            label = stringResource(R.string.ora_fine),
                            onClick = { time ->
                                viewModel.updateEnd(time)
                            }
                        )
                    }
                    OutlinedTextField(
                        value = it.movingTime,
                        onValueChange = { viewModel.updateMovingTime(it) },
                        label = { Text(text = stringResource(R.string.ore_spostamento)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )
                    OutlinedTextField(
                        value = it.km,
                        onValueChange = { viewModel.updateKm(it) },
                        label = { Text(text = stringResource(R.string.km_percorsi)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
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
) {
    if (saveCondition()) {
        saveAction()
    } else {
        elseBranch()
    }
}