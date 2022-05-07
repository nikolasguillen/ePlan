package com.example.eplan.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.User
import com.example.eplan.domain.util.WarningUnit
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@Composable
fun AppointmentDetail(
    viewModel: AppointmentDetailViewModel
) {

    val context = LocalContext.current

    viewModel.appointment.value?.let { appointment ->

        OutlinedTextField(
            value = appointment.title,
            onValueChange = { /*TODO*/ },
            label = { Text(text = stringResource(id = R.string.attivita)) })
        OutlinedTextField(
            value = appointment.description,
            onValueChange = { /*TODO*/ },
            label = { Text(text = stringResource(id = R.string.descrizione)) })
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomDateButton(
                date = appointment.date,
                onDateSelected = {/*TODO*/ }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTimeButton(
                time = appointment.start.toString(),
                label = stringResource(id = R.string.ora_inizio),
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(4F)
            )
            Spacer(modifier = Modifier.weight(1F))
            CustomTimeButton(
                time = appointment.end.toString(),
                label = stringResource(id = R.string.ora_inizio),
                onClick = { /*TODO*/ },
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
            Switch(checked = appointment.planning, onCheckedChange = {/*TODO*/ })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Contabilizza come intervento")
            Switch(checked = appointment.intervention, onCheckedChange = {/*TODO*/ })
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Invita anche:")
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { /*TODO al click deve aprire il dialog con la lista di persone da invitare*/ }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                if (appointment.invited.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.nessun_invitato),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    )
                } else {
                    /*TODO nella lista "invited" dell'appuntamento ho solo le persone effettivamente invitate,
                    *  quindi per popolare la lista delle persone da poter invitare devo prendere i dati da un'altra chiamata
                    **/
                    val people = mutableListOf<User>()
                    for (i in 1..10) {
                        people.add(User(id = i.toString(), fullName = "Persona $i"))
                    }
                    people.forEach {

                    }
                    /**----------**/
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
                modifier = Modifier.weight(1f),
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
                    DropdownMenu(
                        expanded = false /*TODO mettere booleano per apertura/chiusura dropdown menu*/,
                        onDismissRequest = { /*TODO*/ }) {

                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.fine_periodicita))
                CustomDateButton(
                    date = appointment.periodicityEnd,
                    onDateSelected = { date -> /*TODO Cambiare periodicity end al nuovo valore*/ })
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
                onCheckedChange = {/*TODO modificare appointment.memo al click*/ })
        }
        AnimatedVisibility(visible = true) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Avvisami tramite email")
                    /*TODO capire come gestire notifiche push / mail*/
                    /*Switch(checked = appointment, onCheckedChange = )*/
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
                ) {
                    Text(text = "Con un preavviso di:")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var test by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            value = appointment.warningTime.toString(),
                            onValueChange = { /*TODO cambiare tempo preavviso appuntamento*/ },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(6f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.weight(1F))
                        Box(modifier = Modifier.weight(3F)) {
                            OutlinedTextField(
                                value = appointment.warningUnit.getName(context),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = ""
                                    )
                                }
                            )
                            Box(modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .clickable { test = !test }
                                .matchParentSize())
                            DropdownMenu(expanded = test, onDismissRequest = { test = !test }) {
                                DropdownMenuItem(
                                    text = { Text(text = WarningUnit.MINUTES.getName(context)) },
                                    onClick = { /*TODO*/ }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = WarningUnit.HOURS.getName(context)) },
                                    onClick = { /*TODO*/ }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = WarningUnit.DAYS.getName(context)) },
                                    onClick = { /*TODO*/ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    /*if (timeUnitDialog.value) {
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