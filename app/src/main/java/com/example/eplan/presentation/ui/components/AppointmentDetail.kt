package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.domain.model.User
import com.example.eplan.domain.util.WarningUnit
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent.*
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@Composable
fun AppointmentDetail(
    viewModel: AppointmentDetailViewModel
) {
    viewModel.appointment.value?.let { appointment ->

        val context = LocalContext.current
        val currentColor =
            if (appointment.memo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38F
            )
        var showTimeUnitDropdown by remember { mutableStateOf(false) }
        var showInvitedDialog by remember { mutableStateOf(false) }
        var showPeriodicityDialog by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = appointment.activityName,
            onValueChange = { viewModel.onFormEvent(ActivityNameChanged(it)) },
            label = { Text(text = stringResource(id = R.string.attivita)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = appointment.description,
            onValueChange = { viewModel.onFormEvent(DescriptionChanged(it)) },
            label = { Text(text = stringResource(id = R.string.descrizione)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomDateButton(
                date = appointment.date,
                onDateSelected = { viewModel.onFormEvent(DateChanged(it)) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTimeButton(
                time = appointment.start.toString(),
                label = stringResource(id = R.string.ora_inizio),
                onClick = { viewModel.onFormEvent(StartChanged(it)) },
                modifier = Modifier.weight(4F)
            )
            Spacer(modifier = Modifier.weight(1F))
            CustomTimeButton(
                time = appointment.end.toString(),
                label = stringResource(id = R.string.ora_inizio),
                onClick = { viewModel.onFormEvent(EndChanged(it)) },
                modifier = Modifier.weight(4F)
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
            Switch(
                checked = appointment.planning,
                onCheckedChange = { viewModel.onFormEvent(PlanningChanged(it)) })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Contabilizza come intervento")
            Switch(
                checked = appointment.intervention,
                onCheckedChange = { viewModel.onFormEvent(InterventionChanged(it)) })
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Invita anche:")
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { showInvitedDialog = !showInvitedDialog }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                if (appointment.invited.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.nessun_invitato),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    )
                } else {
                    appointment.invited.forEach {
                        Text(
                            text = it.fullName,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium)
                        )
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(4F),
            ) {
                Text(text = stringResource(R.string.periodicita))
                Card(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .clickable { /*TODO aprire dialog periodicità*/ }
                ) {
                    Text(
                        text = appointment.periodicity.getName(context),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(4F)
            ) {
                Text(text = stringResource(R.string.fine_periodicita))
                CustomDateButton(
                    date = appointment.periodicityEnd,
                    onDateSelected = { date -> viewModel.onFormEvent(PeriodicityEndChanged(date)) },
                    showLiteralDate = false
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Attiva promemoria")
            Switch(
                checked = appointment.memo,
                onCheckedChange = { viewModel.onFormEvent(MemoChanged(it)) })
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Avvisami tramite email", color = currentColor)
                /*TODO capire come gestire notifiche push / mail*/
                /*Switch(checked = appointment, onCheckedChange = )*/
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            ) {
                Text(text = "Con un preavviso di:", color = currentColor)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = appointment.warningTime.toString(),
                        onValueChange = { viewModel.onFormEvent(WarningTimeChanged(it.toInt())) },
                        enabled = appointment.memo,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(6F),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Box(modifier = Modifier.weight(5F)) {
                        OutlinedTextField(
                            value = appointment.warningUnit.getName(context),
                            onValueChange = {},
                            enabled = appointment.memo,
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = stringResource(R.string.espandi_unità_tempo)
                                )
                            }
                        )
                        val boxModifier =
                            if (appointment.memo) Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .clickable {
                                    showTimeUnitDropdown = !showTimeUnitDropdown
                                } else Modifier
                        Box(
                            modifier = boxModifier
                                .matchParentSize()
                        )
                        DropdownMenu(
                            expanded = showTimeUnitDropdown,
                            onDismissRequest = { showTimeUnitDropdown = !showTimeUnitDropdown }) {
                            WarningUnit.values().forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.getName(context = context)) },
                                    onClick = {
                                        viewModel.onFormEvent(WarningUnitChanged(it))
                                        showTimeUnitDropdown = !showTimeUnitDropdown
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }


        /** Dialog degli invitati **/
        if (showInvitedDialog) {
            //TODO questa lista di gente andrà presa da una chiamata al server
            val people = mutableListOf<User>()
            for (i in 1..10) {
                people.add(User(id = i.toString(), fullName = "Utente $i"))
            }
            Dialog(onDismissRequest = { showInvitedDialog = !showInvitedDialog }) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.extraLarge
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)
                    ) {
                        Text(
                            text = "Scegli chi invitare",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            modifier = Modifier.heightIn(max = 500.dp)
                        ) {
                            items(people) { person ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = person.fullName,
                                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                                    )
                                    Checkbox(
                                        checked = appointment.invited.contains(person),
                                        onCheckedChange = {
                                            if (appointment.invited.contains(person)) {
                                                viewModel.onFormEvent(RemoveInvited(person))
                                            } else {
                                                viewModel.onFormEvent(AddInvited(person))
                                            }
                                        },
                                        modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                viewModel.onFormEvent(DismissInvitedList)
                                showInvitedDialog = !showInvitedDialog
                            }) {
                                Text(text = stringResource(id = R.string.annulla))
                            }
                            Button(onClick = {
                                viewModel.onFormEvent(ConfirmInvitedList)
                                showInvitedDialog = !showInvitedDialog
                            }) {
                                Text(text = stringResource(id = R.string.conferma))
                            }
                        }
                    }
                }
            }
        }
    }


    /*
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
                            snapshot.dispose()
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
    }*/
}