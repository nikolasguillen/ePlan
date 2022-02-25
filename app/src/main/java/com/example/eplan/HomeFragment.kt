package com.example.eplan

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OurAppTheme {
                    PageComposer()
                }
            }
        }
    }

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PageComposer() {

        val cards = remember { mutableListOf<List<String>>() }

        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))


        Scaffold(
            bottomBar = { BottomNavBar(findNavController()) },
            topBar = { TopBar() },
            floatingActionButton = { FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
            },
            content = {
                Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                    setupCalendar()
                    LazyColumn() {
                        items(cards) { card ->
                            ActivityCard(
                                activityName = card[0],
                                activityDescription = card[1],
                                start = card[2],
                                end = card[3],
                                oreSpostamento = card[4],
                                km = card[5]
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun setupCalendar() {

        val todayBackgroundColor = MaterialTheme.colorScheme.primary.toArgb()
        val todayTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()
        val selectedDayBackgroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
        val selectedDayTextColor = MaterialTheme.colorScheme.surface.toArgb()
        val dynamicTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

        AndroidView(factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                textColor = dynamicTextColor
                setExpandIconColor(dynamicTextColor)

                // Material dynamic theme
                AppCompatResources.getDrawable(context, R.drawable.selection_circle)?.setTint(selectedDayBackgroundColor)
                selectedItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                selectedItemTextColor = selectedDayTextColor
                AppCompatResources.getDrawable(context, R.drawable.today_circle)?.setTint(todayBackgroundColor)
                todayItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.today_circle)
                todayItemTextColor = todayTextColor

                setCalendarListener(object : CollapsibleCalendar.CalendarListener {
                    override fun onClickListener() {
                    }

                    override fun onDataUpdate() {
                    }

                    override fun onDayChanged() {
                    }

                    override fun onDaySelect() {
                        day = selectedDay
                        collapse(100)
                    }

                    override fun onItemClick(v: View) {
                    }

                    override fun onMonthChange() {
                    }

                    override fun onWeekChange(position: Int) {
                    }

                })
            }
        },
            modifier = (Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 5.dp))
        )
    }

    @Composable
    private fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
    }

    @Composable
    private fun BottomNavBar(navController: NavController) {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Appointments,
            NavigationItem.Account
        )
        NavigationBar() {
            items.forEach { item ->
                NavigationBarItem(
                    selected = (item.title == "Foglio ore"),
                    onClick = {  },
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    modifier = Modifier.background(
                        colorResource(id = R.color.transparent),
                        CircleShape
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ActivityCard(activityName: String, activityDescription: String, start: String, end: String, oreSpostamento: String, km: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    findNavController().navigate(HomeFragmentDirections.viewActivityDetails(activityName, activityDescription, start, end, oreSpostamento, km))
                },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = activityName.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
                Text(text = activityDescription.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 3.dp))
                Text(text = start, style =  MaterialTheme.typography.labelSmall)
                Text(text = end, style =  MaterialTheme.typography.labelSmall)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ActivityDetails(navController: NavController, activityName: String, activityDescription: String, start: String, end: String, oreSpostamento: String, km: String) {

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

        var openDialog = remember { mutableStateOf(false) }

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
                            onClick = { navController.popBackStack() },
                            icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                            label = { Text(text = item.title) },
                            modifier = Modifier.background(Color.Transparent, CircleShape)
                        )
                    }
                }
            },
            content = {
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
                        navController.popBackStack()
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

    inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }
}