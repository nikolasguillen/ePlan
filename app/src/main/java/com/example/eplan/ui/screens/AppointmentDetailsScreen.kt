package com.example.eplan.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.eplan.model.Appointment
import com.example.eplan.ui.items.CustomTextField
import com.example.eplan.ui.items.CustomTimeButton
import com.example.eplan.ui.items.SaveItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    appointment: Appointment,
    navController: NavHostController
) {

    val name = remember { mutableStateOf(appointment.activity) }
    val title = remember { mutableStateOf(appointment.title) }
    val desc = remember { mutableStateOf(appointment.description) }
    val date = remember { mutableStateOf(appointment.date) }
    val start = remember { mutableStateOf(appointment.start) }
    val end = remember { mutableStateOf(appointment.end) }
    val planning = remember { mutableStateOf(appointment.planning) }
    val intervention = remember { mutableStateOf(appointment.intervention) }
    val invited = remember { mutableStateOf(appointment.invited) }
    val periodicity = remember { mutableStateOf(appointment.periodicity) }
    val periodicityEnd = remember { mutableStateOf(appointment.periodicityEnd) }
    val memo = remember { mutableStateOf(appointment.memo) }
    val warningTime = remember { mutableStateOf(appointment.warningTime) }
    val warningUnit = remember { mutableStateOf(appointment.warningUnit) }

    val items = listOf(
        SaveItems.Save,
    )

    val backDialog = remember { mutableStateOf(false) }
    val invitedDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Appuntamento") },
                navigationIcon = {
                    IconButton(onClick = { backDialog.value = true }) {
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
            BackHandler(enabled = true) {
                backDialog.value = true
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = it.calculateBottomPadding() + 8.dp
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CustomTextField(value = name, label = "Attività")
                CustomTextField(value = title, label = "Titolo")
                CustomTextField(value = desc, label = "Descrizione")
                CustomTextField(value = date, label = "Data")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomTimeButton(
                        time = start,
                        label = "Ora inizio",
                        context = navController.context
                    )
                    CustomTimeButton(
                        time = end,
                        label = "Ora fine",
                        context = navController.context
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Pianificazione")
                    Checkbox(checked = planning.value, onCheckedChange = { planning.value = it })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Contabilizza come intervento")
                    Checkbox(
                        checked = intervention.value,
                        onCheckedChange = { intervention.value = it })
                }
//                Box(
//                    modifier = Modifier
//                        .border(
//                            BorderStroke(
//                                1.dp,
//                                MaterialTheme.colorScheme.onSurface
//                            ), RoundedCornerShape(8.dp)
//                        )
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                ) {
//                    Column() {
//                        Text(text = "Invita anche:", style = MaterialTheme.typography.displaySmall)
                        var people = ""
                        for (person in invited.value) {
                            people += person + "\n"
                        }
//                        Text(text = people)
                Text(text = "Invita anche")
                Card() {
                    Text(text = people)
                }
                    
//                    }
                }
//            }
        }
    )


    if (invitedDialog.value) {
        Dialog(onDismissRequest = { invitedDialog.value = false }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(600.dp)
            ) {
                Column() {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .wrapContentSize()
                            .weight(1f)
                            .padding(bottom = 6.dp)
                    ) {
                        items(invited.value) { person ->
                            Text(text = person)
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Annulla")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Conferma")
                        }
                    }
                }
            }
        }
    }

    if (backDialog.value) {
        AlertDialog(
            onDismissRequest = {
                backDialog.value = false
            },
            title = { Text(text = "Sei sicuro di voler uscire senza salvare?") },
            confirmButton = {
                TextButton(onClick = {
                    backDialog.value = false
                    navController.navigateUp()
                }
                ) {
                    Text(text = "Conferma")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    backDialog.value = false
                }
                ) {
                    Text(text = "Annulla")
                }
            })
    }
}