package com.example.eplan.presentation.ui.appointment

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.Person
import com.example.eplan.presentation.ui.items.*

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
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
    val people = remember { createPeople(appointment.invited) }
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
    val periodicityDialog = remember { mutableStateOf(false) }
    val timeUnitDialog = remember { mutableStateOf(false) }

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
                            tint = Color.Red,
                            contentDescription = "Elimina commessa"
                        )
                    }
                })
        },
        bottomBar = {
            NavigationBar {
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
        content = { it ->
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
                CustomInputText(value = name, label = "Attività")
                CustomInputText(value = title, label = "Titolo")
                CustomInputText(value = desc, label = "Descrizione")
                CustomInputText(value = date, label = "Data")
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Pianificazione")
                        Text(
                            text = "Non blocca l'orario e non viene esportato",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    CustomSwitch(planning)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Contabilizza come intervento")
                    CustomSwitch(intervention)
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Invita anche:")
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(11.dp))
                            .clickable { invitedDialog.value = true }
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        if (people.all { person ->
                                !person.isChecked.value
                            }) {
                            Text(
                                text = stringResource(R.string.nessun_invitato),
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            for (person in people) {
                                if (person.isChecked.value) {
                                    Text(text = person.name, modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(R.string.periodicita))
                        Card(
                            modifier = Modifier
                                .clip(RoundedCornerShape(11.dp))
                                .fillMaxWidth()
                                .clickable { periodicityDialog.value = true }
                        ) {
                            Text(text = periodicity.value, modifier = Modifier.padding(16.dp))
                        }
                        /*CustomInputDropDown(
                            value = periodicity,
                            items = appointment.getPeriodicities(),
                            enabled = memo,
                            size = Modifier.fillMaxWidth()
                        )*/
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.fine_periodicita))
                        CustomDateButton(date = date, context = navController.context)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Attiva promemoria")
                    CustomSwitch(memo)
                }
                AnimatedVisibility(visible = memo.value) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val test = remember { mutableStateOf(true) }
                            Text(text = "Avvisami tramite email")
                            CustomSwitch(test, memo)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Con un preavviso di:")
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = warningTime.value,
                                    onValueChange = { warningTime.value = it },
                                    label = { },
                                    readOnly = !memo.value,
                                    textStyle = MaterialTheme.typography.bodyLarge,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        backgroundColor = Color.Transparent,
                                        disabledTextColor = Color.Transparent
                                    ),
                                    modifier = Modifier.weight(6f),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                CustomInputDropDown(
                                    value = warningUnit,
                                    items = mutableListOf("minuti", "ore", "giorni"),
                                    enabled = memo
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    if (timeUnitDialog.value) {
        Dialog(onDismissRequest = { timeUnitDialog.value = false }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            ) {
                Column {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(listOf("minuti", "ore", "giorni")) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable {
                                        warningUnit.value = item
                                        timeUnitDialog.value = false
                                    }
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (invitedDialog.value) {
        val snapshot = Snapshot.takeSnapshot()
        Dialog(onDismissRequest = { invitedDialog.value = false }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp)
                    .height(500.dp)
            ) {
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 10.dp)
                    ) {
                        items(people) { person ->
                            val checkedState = remember { person.isChecked }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        checkedState.value = !checkedState.value
                                        person.isChecked = checkedState
                                    }
                            ) {
                                Text(
                                    text = person.name,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        top = 12.dp,
                                        bottom = 12.dp
                                    )
                                )
                                Checkbox(checked = checkedState.value, onCheckedChange = {
                                    checkedState.value = it
                                    person.isChecked = checkedState
                                }, modifier = Modifier.padding(end = 16.dp))
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            for (person in people) {
                                person.isChecked.value = snapshot.enter { person.isChecked.value }
                            }
                            invitedDialog.value = false
                        }) {
                            Text(text = stringResource(R.string.annulla))
                        }
                        Button(onClick = { invitedDialog.value = false }) {
                            Text(text = stringResource(R.string.conferma))
                        }
                    }
                }
            }
        }
    }

    if (periodicityDialog.value) {
        Dialog(onDismissRequest = { periodicityDialog.value = false }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            ) {
                Column {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(Appointment.Periodicity.values()) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable {
                                        periodicity.value = item.toString()
                                        periodicityDialog.value = false
                                    }
                            ) {
                                Text(
                                    text = item.toString(),
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    )
                                )
                            }
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
            title = { Text(text = stringResource(R.string.chiudi_senza_salvare)) },
            confirmButton = {
                TextButton(onClick = {
                    backDialog.value = false
                    navController.navigateUp()
                }
                ) {
                    Text(text = stringResource(id = R.string.conferma))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    backDialog.value = false
                }
                ) {
                    Text(text = stringResource(id = R.string.annulla))
                }
            })
    }
}

fun createPeople(people: Map<String, Boolean>): MutableList<Person> {
    val list = mutableListOf<Person>()
    for (person in people) {
        list.add(Person(person.key, mutableStateOf(person.value)))
    }
    return list
}