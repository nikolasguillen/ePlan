package com.example.eplan

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class ActivityDetailsFragment: Fragment() {

    private val ourDarkColorScheme = darkColorScheme()

    private val ourLightColorScheme = lightColorScheme()

    @Composable
    fun OurAppTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val ourColorScheme = if (darkTheme) ourDarkColorScheme else ourLightColorScheme

        MaterialTheme(
            content = content,
            colorScheme = ourColorScheme
        )
    }

    val args: ActivityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OurAppTheme() {
                    ActivityDetails(
                        activityName = args.activityName,
                        activityDescription = args.activityDescription,
                        start = args.start,
                        end = args.end,
                        oreSpostamento = args.oreSpostamento,
                        km = args.km
                    )
                }
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ActivityDetails(activityName: String, activityDescription: String, start: String, end: String, oreSpostamento: String, km: String) {

        val start = remember { mutableStateOf(start) }
        val end = remember { mutableStateOf(end) }
        val name = remember { mutableStateOf(activityName) }
        val desc = remember { mutableStateOf(activityDescription) }
        val oreSpostamento = remember { mutableStateOf(oreSpostamento) }
        val km = remember { mutableStateOf(km) }

        val items = listOf(
            SaveItems.Save,
//            SaveItems.Cancel
        )

        val openDialog = remember { mutableStateOf(false) }

        Scaffold(
            topBar = { MediumTopAppBar(
                title = { Text(text = "Attività") },
                navigationIcon = {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Elimina commessa")
                    }
                })
            },
            bottomBar = {
                NavigationBar() {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = false,
                            onClick = { findNavController().navigateUp() },
                            icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
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
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    CustomTextField(value = name, label = "Attività")
                    CustomTextField(value = desc, label = "Descrizione")
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(
                            modifier = Modifier.width(150.dp),
                            onClick = { customTimePicker(start) },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onSurfaceVariant))
                        {
                            Text(text = "Ora inizio: " + start.value, style = MaterialTheme.typography.bodyMedium)
                        }
                        OutlinedButton(
                            modifier = Modifier.width(150.dp),
                            onClick = { customTimePicker(end) },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onSurfaceVariant))
                        {
                            Text(text = "Ora fine: " + end.value, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    CustomTextField(value = oreSpostamento, label = "Ore di spostamento", numField = true)
                    CustomTextField(value = km, label = "Km percorsi", numField = true)
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
                        findNavController().navigate(ActivityDetailsFragmentDirections.backHome())
                        }
                    ) {
                        Text(text = "Conferma")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                    }
                    ) {
                        Text(text = "Annulla")
                    }
                })
        }
    }

    @Composable
    private fun CustomTextField(value: MutableState<String>, label: String, numField: Boolean = false) {
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

    private fun customTimePicker(time: MutableState<String>) {

        var newTime = time

        val timePickerDialog = TimePickerDialog(requireContext(), R.style.MyTimePickerDialogStyle,
            { _, hour: Int, minute: Int ->
                newTime.value = String.format("%02d", hour) + ":" + String.format("%02d", minute)
            }, Integer.parseInt(newTime.value.split(":")[0]), Integer.parseInt(newTime.value.split(":")[1]), true)

        timePickerDialog.show()

    }
}