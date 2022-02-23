package com.example.eplan

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

var day: Day? = null
var selectedDay: Int = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { HomeBuilder() }
    }


    @Composable
    fun HomeBuilder() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                BuildContent(navController)
            }
            composable("activityDetails/{navController}/{activityName}/{activityDescription}/{start}/{end}") { backStackEntry ->
                ActivityDetails(
                    navController,
                    backStackEntry.arguments?.getString("activityName")!!,
                    backStackEntry.arguments?.getString("activityDescription")!!,
                    backStackEntry.arguments?.getString("start")!!,
                    backStackEntry.arguments?.getString("end")!!
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BuildContent(navController: NavController) {
        Scaffold(modifier = Modifier.padding(horizontal = 2.dp),
                bottomBar = { BottomNavBar(navController) },
                topBar = { TopBar() },
                content = {
                    Column() {
                        setupCalendar()
                        ActivityCard(navController, "attività", "descrizione",
                            "15:30", "16:00")
                        }
                    })
    }

    @Composable
    private fun setupCalendar() {

        AndroidView(factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                todayItemTextColor = resources.getColor(R.color.black, context.theme)
                selectedItemTextColor = resources.getColor(R.color.white, context.theme)
                selectedItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.black_circle)
                todayItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.light_purple_circle)

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
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) }, )
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
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title)},
                    label = { Text(text = item.title)},
                    modifier = Modifier.background(colorResource(id = R.color.transparent),
                        CircleShape)
                    )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ActivityCard(navController: NavController, activityName: String, activityDescription: String, start: String, end: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    navController.navigate("activityDetails/${navController}/${activityName}/${activityDescription}/${start}/${end}")
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
    private fun ActivityDetails(navController: NavController, activityName: String, activityDescription: String, start: String, end: String) {

        val items = listOf(
            SaveItems.Save,
            SaveItems.Cancel
        )

        var openDialog = remember { mutableStateOf(false) }
        
        Scaffold(
            topBar = { MediumTopAppBar(
                title = { Text(text = "Attività") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                }) },
            bottomBar = {
                NavigationBar() {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = false,
                            onClick = { if (item.title == "Scarta modifiche") openDialog.value = true },
                            icon = { Icon(painterResource(id = item.icon), contentDescription = item.title)},
                            label = { Text(text = item.title)},
                            modifier = Modifier.background(colorResource(id = R.color.transparent),
                                CircleShape)
                        )
                    }
                }
            },
            content = {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)) {

                    CustomTextField(value = activityName, label = "Attività")
                    CustomTextField(value = activityDescription, label = "Descrizione")
                    CustomTextField(value = start, label = "Ora inizio")
                    CustomTextField(value = end, label = "Ora fine")
                }
            })

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = { Text(text = "Sei sicuro di voler uscire senza salvare?")},
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
    private fun CustomTextField(value: String, label: String) {
        var value = value
        TextField(
            value = value,
            onValueChange = { value = it },
            label = { Text(text = label) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}