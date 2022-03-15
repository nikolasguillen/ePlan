package com.example.eplan.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.model.Person
import com.example.eplan.model.WorkActivity
import com.example.eplan.ui.items.CustomSwitch
import com.example.eplan.ui.items.CustomTextField
import com.example.eplan.ui.items.CustomTimeButton
import com.example.eplan.ui.items.SaveItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailsScreen(
    workActivity: WorkActivity,
    navController: NavHostController
) {

    val start = remember { mutableStateOf(workActivity.start) }
    val end = remember { mutableStateOf(workActivity.end) }
    val name = remember { mutableStateOf(workActivity.activityName) }
    val desc = remember { mutableStateOf(workActivity.activityDescription) }
    val movingTime = remember { mutableStateOf(workActivity.movingTime) }
    val km = remember { mutableStateOf(workActivity.km) }
    val close = remember { mutableStateOf(workActivity.close) }

    val items = listOf(
        SaveItems.Save,
    )

    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Attività") },
                navigationIcon = {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = Color.Red,
                            contentDescription = "Elimina commessa"
                        )
                    }
                })
        },
        bottomBar = {
            NavigationBar() {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigateUp() },
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
                CustomTextField(value = name, label = "Attività")
                CustomTextField(value = desc, label = "Descrizione")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomTimeButton(time = start, label = "Ora inizio", context = navController.context)
                    CustomTimeButton(time = end, label = "Ora fine", context = navController.context)
                }
                CustomTextField(
                    value = movingTime,
                    label = "Ore di spostamento",
                    numField = true
                )
                CustomTextField(value = km, label = "Km percorsi", numField = true)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Chiudi attività")
                    CustomSwitch(close)
                }
            }
        })

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Sei sicuro di voler uscire senza salvare?") },
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