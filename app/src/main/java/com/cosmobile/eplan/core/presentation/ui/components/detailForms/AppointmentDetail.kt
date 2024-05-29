package com.cosmobile.eplan.core.presentation.ui.components.detailForms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.domain.model.User
import com.cosmobile.eplan.appointment_detail.ui.AppointmentDetailUiState
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnConfirmUsersList
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDateChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDescriptionChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnDismissUsersList
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnEndTimeChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnInterventionChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnPeriodicityChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnPlanningChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnStartTimeChanged
import com.cosmobile.eplan.appointment_detail.ui.AppointmentFormEvent.OnUserSelected
import com.cosmobile.eplan.appointment_detail.ui.UserUiState
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent
import com.cosmobile.eplan.core.presentation.ui.WorkActivityFormEvent.OnToggleActivitySelectorVisibility
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomDateButton
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomDialog
import com.cosmobile.eplan.core.presentation.ui.components.uiElements.CustomTimeButton
import com.cosmobile.eplan.core.util.spacing

@ExperimentalMaterial3Api
@Composable
fun AppointmentDetail(
    state: AppointmentDetailUiState,
    onFormEvent: (WorkActivityFormEvent) -> Unit
) {
    state.workActivity?.let { appointment ->

        val context = LocalContext.current
        val currentColor =
            if (appointment.memo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38F
            )
        var showTimeUnitDropdown by remember { mutableStateOf(false) }
        var showInvitedDialog by remember { mutableStateOf(false) }
        var showPeriodicityDialog by remember { mutableStateOf(false) }

        ActivitySelectorCard(
            workActivity = appointment,
            suggestions = state.activitySelectorUiState.activities.take(5),
            onSuggestionSelected = { onFormEvent(WorkActivityFormEvent.OnActivitySelected(it)) },
            modifier = Modifier
                .clickable { onFormEvent(OnToggleActivitySelectorVisibility) }
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Column {
            OutlinedTextField(
                value = appointment.description,
                onValueChange = { onFormEvent(OnDescriptionChanged(it)) },
                label = { Text(text = stringResource(id = R.string.descrizione)) },
                keyboardOptions = KeyboardOptions(
                    capitalization =
                    KeyboardCapitalization.Sentences
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (appointment.descriptionError != null) {
                Text(
                    text = appointment.descriptionError.asString(context),
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
                onDateSelected = { onFormEvent(OnDateChanged(it)) }
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomTimeButton(
                    time = appointment.start,
                    label = stringResource(id = R.string.ora_inizio),
                    onClick = { onFormEvent(OnStartTimeChanged(it)) },
                    modifier = Modifier.weight(4F)
                )
                Spacer(modifier = Modifier.weight(1F))
                CustomTimeButton(
                    time = appointment.end,
                    label = stringResource(id = R.string.ora_inizio),
                    onClick = { onFormEvent(OnEndTimeChanged(it)) },
                    modifier = Modifier.weight(4F)
                )
            }
            if (appointment.timeError != null) {
                Text(
                    text = appointment.timeError.asString(context),
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
                onCheckedChange = { onFormEvent(OnPlanningChanged(it)) })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.contabilizza_intervento))
            Switch(
                checked = appointment.accounted,
                onCheckedChange = { onFormEvent(OnInterventionChanged(it)) })
        }

        // TODO prima o poi implementare la lista degli invitati
        /*Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
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
        }*/


        // TODO la periodicità non funziona nemmeno da web, finché non viene sistemata la nascondiamo anche qua
        /*Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                        text = stringResource(id = appointment.periodicity.humanReadableNameResId),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1F))

            if (appointment.periodicity !is Periodicity.None) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier.weight(4F)
                ) {
                    Text(text = stringResource(R.string.fine_periodicita))
                    CustomDateButton(
                        date = appointment.periodicityEnd ?: LocalDate.now(),
                        onDateSelected = { date -> onFormEvent(OnPeriodicityEndChanged(date)) },
                        showLiteralDate = false
                    )
                }
            }
        }*/

        // TODO prima o poi implementare promemoria e notifiche
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.attiva_promemoria))
            Switch(
                checked = appointment.memo,
                onCheckedChange = { onFormEvent(OnMemoChecked) })
        }
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.avviso_email), color = currentColor)
                *//*TODO capire come gestire notifiche push / mail*//*
                *//*Switch(checked = appointment, onCheckedChange = )*//*
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
                        onValueChange = { onFormEvent(OnWarningTimeChanged(it.toInt())) },
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
                            WarningUnit.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.getName(context = context)) },
                                    onClick = {
                                        onFormEvent(OnWarningUnitChanged(it))
                                        showTimeUnitDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }*/


        /* Dialog degli invitati */
        if (showInvitedDialog) {
            //TODO questa lista di gente andrà presa da una chiamata al server
            val users = mutableListOf<UserUiState>()
            for (i in 1..20) {
                users.add(UserUiState(User(id = i.toString(), fullName = "Utente $i"), false))
            }
            CustomDialog(
                title = stringResource(id = R.string.scegli_invitati),
                onDismissRequest = {
                    onFormEvent(OnDismissUsersList)
                    showInvitedDialog = false
                },
                onConfirmationRequest = {
                    onFormEvent(OnConfirmUsersList)
                    showInvitedDialog = false
                }) {
                LazyColumn(
                    modifier = Modifier.heightIn(
                        max = LocalConfiguration.current.screenHeightDp.dp.times(
                            0.65F
                        )
                    )
                ) {
                    items(users) { userUiState ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { userUiState.user?.let { onFormEvent(OnUserSelected(it)) } }
                        ) {
                            Text(
                                text = userUiState.user?.fullName ?: "",
                                modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                            )
                            Checkbox(
                                checked = userUiState.isSelected,
                                onCheckedChange = {
                                    userUiState.user?.let { user ->
                                        onFormEvent(
                                            OnUserSelected(user)
                                        )
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
                    items(state.periodicityList) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFormEvent(OnPeriodicityChanged(it))
                                    showPeriodicityDialog = false
                                }
                        ) {
                            RadioButton(
                                selected = it == appointment.periodicity,
                                onClick = {
                                    onFormEvent(OnPeriodicityChanged(it))
                                    showPeriodicityDialog = false
                                }
                            )
                            Text(
                                text = stringResource(id = it.humanReadableNameResId)
                            )
                        }
                    }
                }
            }
        }
    }
}