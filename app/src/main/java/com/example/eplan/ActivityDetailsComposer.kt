package com.example.eplan

import android.app.TimePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.eplan.ui.SaveItems

val peopleInput = listOf(
    "Giampiero Allamprese",
    "Emanuele Crescentini",
    "Nikolas Guillen Leon",
    "Giorgio Pierantoni",
    "Natalia Diaz",
    "Marco Zaccheroni",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon",
    "Nikolas Guillen Leon"
)

var people: MutableList<Person> = mutableListOf()

fun PeopleList(peopleInput: List<String>) {
    for (person in peopleInput) {
        people.add(Person(person, false))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetails(
    activityName: String,
    activityDescription: String,
    date: String,
    start: String,
    end: String,
    oreSpostamento: String,
    km: String,
    close: Boolean,
    navController: NavHostController
) {

    val start = remember { mutableStateOf(start) }
    val end = remember { mutableStateOf(end) }
    val name = remember { mutableStateOf(activityName) }
    val desc = remember { mutableStateOf(activityDescription) }
    val oreSpostamento = remember { mutableStateOf(oreSpostamento) }
    val km = remember { mutableStateOf(km) }

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
                CustomTextField(
                    value = oreSpostamento,
                    label = "Ore di spostamento",
                    numField = true
                )
                CustomTextField(value = km, label = "Km percorsi", numField = true)
            }
        })

    if (openDialog.value) {
//        AlertDialog(
//            onDismissRequest = {
//                openDialog.value = false
//            },
//            title = { Text(text = "Sei sicuro di voler uscire senza salvare?") },
//            confirmButton = {
//                TextButton(onClick = {
//                    openDialog.value = false
//                    navController.navigateUp()
//                }
//                ) {
//                    Text(text = "Conferma")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = {
//                    openDialog.value = false
//                }
//                ) {
//                    Text(text = "Annulla")
//                }
//            }
//        )

        PeopleList(peopleInput = peopleInput)
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .height(500.dp)
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(people) { person ->
                        person.PersonComposer()
                    }
                }
            }
        }
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

    val timePickerDialog = TimePickerDialog(
        context,
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

class Person(name: String, isChecked: Boolean = false) {

    private var isChecked = isChecked
    private val name = name

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PersonComposer() {
        val checkedState = remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = name)
            Checkbox(checked = checkedState.value, onCheckedChange = {
                checkedState.value = it
                isChecked = checkedState.value
            })
        }
    }

    fun getCheckedState(): Boolean {
        return isChecked
    }
}