package com.example.eplan.presentation.ui.workActivity

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.components.CustomTimeButton
import com.example.eplan.presentation.ui.workActivity.ActivityDetailEvent.GetActivityEvent
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    activityId: String,
    onBackPressed: () -> Unit,
    onSavePressed: (WorkActivity) -> Unit,
    onDeletePressed: (String) -> Unit
) {

    val onLoad = viewModel.onLoad.value

    // Evita di rifare la chiamata API ad ogni recomposition
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(GetActivityEvent(activityId))
    }

    val workActivity = remember { viewModel.workActivity.value }

    val bottomBarState = rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val items = listOf(
        BottomNavBarItems.Save,
    )

    val backDialog = remember { mutableStateOf(false) }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.errore_orario),
        Toast.LENGTH_SHORT
    )

    workActivity?.let {

        val title = remember {
            mutableStateOf(it.title)
        }

        val description = remember {
            mutableStateOf(it.description)
        }

        val date = remember {
            mutableStateOf(it.date)
        }

        val start = remember {
            mutableStateOf(it.start)
        }

        val end = remember {
            mutableStateOf(it.end)
        }

        val movingTime = remember {
            mutableStateOf(it.movingTime)
        }

        val km = remember {
            mutableStateOf(it.km)
        }

        Scaffold(
            topBar = {
                MediumTopAppBar(
                    title = { Text(text = stringResource(R.string.attivita)) },
                    navigationIcon = {
                        IconButton(onClick = {

                            var edited = false
                            if ((title.value != it.title) ||
                                (description.value != it.description) ||
                                (start.value != it.start) ||
                                (end.value != it.end) ||
                                (movingTime.value != it.movingTime) ||
                                (km.value != it.km)
                            ) {
                                edited = true
                            }

                            if (edited) {
                                backDialog.value = true
                            } else {
                                bottomBarState.value = false
                                onBackPressed()
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
                            onDeletePressed(activityId)
                            viewModel.onLoad.value = false
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
                NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                if (Duration.between(
                                        LocalTime.parse(
                                            start.value,
                                            DateTimeFormatter.ISO_TIME
                                        ),
                                        LocalTime.parse(
                                            end.value,
                                            DateTimeFormatter.ISO_TIME
                                        )
                                    ).toMinutes() > 0
                                ) {
                                    onSavePressed(
                                        WorkActivity(
                                            id = activityId,
                                            title = title.value,
                                            description = description.value,
                                            date = date.value,
                                            start = start.value,
                                            end = end.value,
                                            km = km.value,
                                            movingTime = movingTime.value
                                        )
                                    )
                                } else {
                                    toast.cancel()
                                    toast.setText(R.string.errore_orario)
                                    toast.show()
                                }
                            },
                            icon = {
                                Icon(
                                    painterResource(id = item.icon),
                                    contentDescription = item.title,
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            modifier = Modifier.background(Color.Transparent, CircleShape)
                        )
                    }
                }
            },
            content = { paddingValues ->
                BackHandler(enabled = true) {

                    var edited = false
                    if ((title.value != it.title) ||
                        (description.value != it.description) ||
                        (start.value != it.start) ||
                        (end.value != it.end) ||
                        (movingTime.value != it.movingTime) ||
                        (km.value != it.km)
                    ) {
                        edited = true
                    }

                    if (edited) {
                        backDialog.value = true
                    } else {
                        onBackPressed()
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
                        value = title.value,
                        onValueChange = { title.value = it },
                        label = { Text(text = stringResource(R.string.attivita)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text(text = stringResource(R.string.descrizione)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomTimeButton(
                            startTime = start.value,
                            label = stringResource(R.string.ora_inizio),
                            onClick = { time ->
                                start.value = time
                            }
                        )
                        CustomTimeButton(
                            startTime = end.value,
                            label = stringResource(R.string.ora_fine),
                            onClick = { time ->
                                end.value = time
                            }
                        )
                    }
                    OutlinedTextField(
                        value = movingTime.value,
                        onValueChange = { movingTime.value = it },
                        label = { Text(text = stringResource(R.string.ore_spostamento)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )
                    OutlinedTextField(
                        value = km.value,
                        onValueChange = { km.value = it },
                        label = { Text(text = stringResource(R.string.km_percorsi)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
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
                    viewModel.onLoad.value = false
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