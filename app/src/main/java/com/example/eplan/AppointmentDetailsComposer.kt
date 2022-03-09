package com.example.eplan

import android.app.TimePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.eplan.ui.SaveItems
import java.util.*

enum class Periodicity {
    Giornaliera, Settimanale, Bisettimanale, Mensile, Bimestrale
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetails(
    activity: String,
    title: String,
    description: String,
    date: String,
    start: String,
    end: String,
    planning: Boolean,
    intervention: Boolean,
    invited: List<String>,
    periodicity: Periodicity,
    periodicityEnd: Date,
    memo: Boolean,
    warningTime: Int,
    warningUnit: String,
    movingTime: String,
    navController: NavHostController
) {

    val name = remember { mutableStateOf(activity) }
    val title = remember { mutableStateOf(title) }
    val desc = remember { mutableStateOf(description) }
    val date = remember { mutableStateOf(date.toString()) }
    val start = remember { mutableStateOf(start) }
    val end = remember { mutableStateOf(end) }
    val planning = remember { mutableStateOf(planning) }
    val intervention = remember { mutableStateOf(intervention) }
    val invited = remember { mutableStateOf(invited) }
    val periodicity = remember { mutableStateOf(periodicity) }
    val periodicityEnd = remember { mutableStateOf(periodicityEnd) }
    val memo = remember { mutableStateOf(memo) }
    val warningTime = remember { mutableStateOf(warningTime) }
    val warningUnit = remember { mutableStateOf(warningUnit) }
    val move = remember { mutableStateOf(movingTime) }

    val items = listOf(
        SaveItems.Save,
    )

    val closeDialog = remember { mutableStateOf(false) }
    val invitedDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Attività") },
                navigationIcon = {
                    IconButton(onClick = { closeDialog.value = true }) {
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
                closeDialog.value = true
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CustomTextField(value = name, label = "Attività")
                CustomTextField(value = title, label = "Titolo")
                CustomTextField(value = date, label = "Data")
                CustomTextField(value = desc, label = "Descrizione")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        modifier = Modifier.width(150.dp),
                        onClick = { customTimePicker(start, navController.context) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    {
                        Text(
                            text = "Ora inizio: " + start.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.width(150.dp),
                        onClick = { customTimePicker(end, navController.context) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    {
                        Text(
                            text = "Ora fine: " + end.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Row(
                ) {
                    Text(text = "Pianificazione")
                    Checkbox(checked = planning.value, onCheckedChange = {})
                }
                Row(
                ) {
                    Text(text = "Contabilizza come intervento")
                    Checkbox(checked = intervention.value, onCheckedChange = {})
                }
                Row() {
                    Text(text = "Invita anche")
                    TextButton(onClick = { invitedDialog.value = true }) {

                    }
                }
                CustomTextField(
                    value = move,
                    label = "Ore di spostamento",
                    numField = true
                )

            }
        })

    if (closeDialog.value) {
        AlertDialog(
            onDismissRequest = {
                closeDialog.value = false
            },
            title = { Text(text = "Sei sicuro di voler uscire senza salvare?") },
            confirmButton = {
                TextButton(onClick = {
                    closeDialog.value = false
                    navController.navigateUp()
                }
                ) {
                    Text(text = "Conferma")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    closeDialog.value = false
                }
                ) {
                    Text(text = "Annulla")
                }
            })
    }
}

@Composable
private fun CustomTextField(
    value: MutableState<String>,
    label: String,
    numField: Boolean = false
) {
    var value = value
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(text = label) },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = Color.Transparent,
            disabledTextColor = Color.Transparent
        ),
        singleLine = numField,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = if (numField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

private fun customTimePicker(time: MutableState<String>, context: Context) {

    var newTime = time

    val timePickerDialog = TimePickerDialog(context,
        R.style.MyTimePickerDialogStyle,
        { _, hour: Int, minute: Int ->
            newTime.value = String.format("%02d", hour) + ":" + String.format("%02d", minute)
        },
        Integer.parseInt(newTime.value.split(":")[0]),
        Integer.parseInt(newTime.value.split(":")[1]),
        true
    )

    timePickerDialog.show()

}