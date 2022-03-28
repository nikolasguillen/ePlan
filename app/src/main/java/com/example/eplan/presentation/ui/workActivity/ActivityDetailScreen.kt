package com.example.eplan.presentation.ui.workActivity

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.items.CustomInputText
import com.example.eplan.presentation.ui.items.CustomTimeButton
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailsScreen(
    viewModel: ActivityDetailViewModel,
    activityId: Int,
    onBackPressed: () -> Unit,
    onSavePressed: (WorkActivity) -> Unit,
    onDeletePressed: () -> Unit
) {



    val start = remember { mutableStateOf(workActivity.start) }
    val end = remember { mutableStateOf(workActivity.end) }
    val date = remember { mutableStateOf(workActivity.date) }
    val title = remember { mutableStateOf(workActivity.title) }
    val desc = remember { mutableStateOf(workActivity.description) }
    val movingTime = remember { mutableStateOf(workActivity.movingTime) }
    val km = remember { mutableStateOf(workActivity.km) }

    val items = listOf(
        BottomNavBarItems.Save,
    )

    val openDialog = remember { mutableStateOf(false) }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.errore_orario),
        Toast.LENGTH_SHORT
    )

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(R.string.attivita)) },
                navigationIcon = {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = Color.Red,
                            contentDescription = stringResource(R.string.elimina_commessa)
                        )
                    }
                })
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (Duration.between(
                                    LocalTime.parse(start.value, DateTimeFormatter.ISO_TIME),
                                    LocalTime.parse(end.value, DateTimeFormatter.ISO_TIME)
                                ).toMinutes() > 0
                            ) {
                                onSavePressed(
                                    WorkActivity(
                                        id = workActivity.id,
                                        title = title.value,
                                        description = desc.value,
                                        date = date.value,
                                        start = start.value,
                                        end = end.value,
                                        km = km.value,
                                        movingTime = movingTime.value
                                    )
                                )
                            } else {
                                toast.cancel()
                                toast.setText(navController.context.getString(R.string.errore_orario))
                                toast.show()
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        modifier = Modifier.background(Color.Transparent, CircleShape)
                    )
                }
            }
        },
        content = {
            it.calculateBottomPadding()
            BackHandler(enabled = true) {
                openDialog.value = true
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = "testo super mega iper lungo",
                    onValueChange = {},
                    label = {
                        Text(
                            text = "Attività"
                        )
                    })
                CustomInputText(value = title, label = stringResource(R.string.attivita))
                CustomInputText(value = desc, label = stringResource(R.string.descrizione))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomTimeButton(
                        time = start,
                        label = stringResource(R.string.ora_inizio),
                        context = navController.context
                    )
                    CustomTimeButton(
                        time = end,
                        label = stringResource(R.string.ora_fine),
                        context = navController.context
                    )
                }
                CustomInputText(
                    value = movingTime,
                    label = stringResource(R.string.ore_spostamento),
                    numField = true
                )
                CustomInputText(
                    value = km,
                    label = stringResource(R.string.km_percorsi),
                    numField = true
                )
            }
        })

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = stringResource(R.string.chiudi_senza_salvare)) },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    navController.navigateUp()
                }
                ) {
                    Text(text = stringResource(R.string.conferma))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }
                ) {
                    Text(text = stringResource(R.string.annulla))
                }
            }
        )
    }
}