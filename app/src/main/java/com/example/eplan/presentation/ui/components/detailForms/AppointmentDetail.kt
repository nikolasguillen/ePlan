package com.example.eplan.presentation.ui.components.detailForms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.User
import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
import com.example.eplan.presentation.ui.appointment.AppointmentDetailViewModel
import com.example.eplan.presentation.ui.appointment.AppointmentFormEvent.*
import com.example.eplan.presentation.ui.components.CustomDateButton
import com.example.eplan.presentation.ui.components.CustomDialog
import com.example.eplan.presentation.ui.components.CustomTimeButton
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

        Column {
            OutlinedTextField(
                value = appointment.activityName,
                onValueChange = { viewModel.onFormEvent(ActivityNameChanged(it)) },
                label = { Text(text = stringResource(id = R.string.attivita)) },
                modifier = Modifier.fillMaxWidth()
            )
            if (appointment.activityIdError != null) {
                Text(
                    text = appointment.activityIdError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column {
            OutlinedTextField(
                value = appointment.description,
                onValueChange = { viewModel.onFormEvent(DescriptionChanged(it)) },
                label = { Text(text = stringResource(id = R.string.descrizione)) },
                modifier = Modifier.fillMaxWidth()
            )
            if (appointment.descriptionError != null) {
                Text(
                    text = appointment.descriptionError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomDateButton(
                date = appointment.date,
                onDateSelected = { viewModel.onFormEvent(DateChanged(it)) }
            )
        }
        Column {
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
            if (appointment.timeError != null) {
                Text(
                    text = appointment.timeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = stringResource(R.string.pianificazione))
                Text(
                    text = stringResource(R.string.sottotitolo_pianificazione),
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
            Text(text = stringResource(R.string.contabilizza_intervento))
            Switch(
                checked = appointment.intervention,
                onCheckedChange = { viewModel.onFormEvent(InterventionChanged(it)) })
        }
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            Text(text = stringResource(R.string.invita_anche))
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { showInvitedDialog = true }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                if (appointment.invited.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.nessun_invitato),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        modifier = Modifier.padding(all = MaterialTheme.spacing.medium)
                    ) {
                        appointment.invited.forEach {
                            Text(text = it.fullName)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.weight(4F),
            ) {
                Text(text = stringResource(R.string.periodicita))
                Card(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .clickable { showPeriodicityDialog = true }
                ) {
                    Text(
                        text = appointment.periodicity.getName(context),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
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
            Text(text = stringResource(R.string.attiva_promemoria))
            Switch(
                checked = appointment.memo,
                onCheckedChange = { viewModel.onFormEvent(MemoChanged(it)) })
        }
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.avviso_email), color = currentColor)
                /*TODO capire come gestire notifiche push / mail*/
                /*Switch(checked = appointment, onCheckedChange = )*/
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            ) {
                Text(text = stringResource(R.string.preavviso), color = currentColor)
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
                                    showTimeUnitDropdown = true
                                } else Modifier
                        Box(
                            modifier = boxModifier
                                .matchParentSize()
                        )
                        DropdownMenu(
                            expanded = showTimeUnitDropdown,
                            onDismissRequest = { showTimeUnitDropdown = false }) {
                            WarningUnit.values().forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.getName(context = context)) },
                                    onClick = {
                                        viewModel.onFormEvent(WarningUnitChanged(it))
                                        showTimeUnitDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }


        /* Dialog degli invitati */
        if (showInvitedDialog) {
            //TODO questa lista di gente andrà presa da una chiamata al server
            val users = mutableListOf<User>()
            for (i in 1..20) {
                users.add(User(id = i.toString(), fullName = "Utente $i"))
            }
            CustomDialog(
                title = stringResource(id = R.string.scegli_invitati),
                onDismissRequest = {
                    viewModel.onFormEvent(DismissInvitedList)
                    showInvitedDialog = false
                },
                onConfirmationRequest = {
                    viewModel.onFormEvent(ConfirmInvitedList)
                    showInvitedDialog = false
                }) {
                LazyColumn(
                    modifier = Modifier.heightIn(
                        max = LocalConfiguration.current.screenHeightDp.dp.times(
                            0.65F
                        )
                    )
                ) {
                    items(users) { user ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (viewModel.isUserInvited(user)) {
                                        viewModel.onFormEvent(RemoveInvited(user))
                                    } else {
                                        viewModel.onFormEvent(AddInvited(user))
                                    }
                                }
                        ) {
                            Text(
                                text = user.fullName,
                                modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                            )
                            Checkbox(
                                checked = viewModel.isUserInvited(user),
                                onCheckedChange = {
                                    if (viewModel.isUserInvited(user)) {
                                        viewModel.onFormEvent(RemoveInvited(user))
                                    } else {
                                        viewModel.onFormEvent(AddInvited(user))
                                    }
                                },
                                modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                            )
                        }
                    }
                }
            }
        }

        /* Dialog periodicità */
        if (showPeriodicityDialog) {
            CustomDialog(
                title = stringResource(id = R.string.scegli_periodicità),
                onDismissRequest = { showPeriodicityDialog = false }) {
                LazyColumn(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(Periodicity.values()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onFormEvent(PeriodicityChanged(it))
                                    showPeriodicityDialog = false
                                }
                        ) {
                            RadioButton(
                                selected = it == appointment.periodicity,
                                onClick = {
                                    viewModel.onFormEvent(PeriodicityChanged(it))
                                    showPeriodicityDialog = false
                                })
                            Text(
                                text = it.getName(context)
                            )
                        }
                    }
                }
            }
        }
    }
}