package com.example.eplan.ui

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.eplan.R
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.time.LocalTime

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
            composable("test") {
                Text(text = "CIAOOO")
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
                        BuildCard(navController, "attività", "descrizione",
                            LocalTime.of(15, 30), LocalTime.of(16, 0),
                            androidx.compose.ui.graphics.Color.Red) }
                    })
    }

    @Composable
    private fun setupCalendar() {

        AndroidView(factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, theme)
                todayItemTextColor = resources.getColor(R.color.black, theme)
                selectedItemTextColor = resources.getColor(R.color.white, theme)
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
    private fun BuildCard(navController: NavController, activityName: String, activityDescription: String, start: LocalTime, end: LocalTime, cardColor: androidx.compose.ui.graphics.Color) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    navController.navigate("test")
                },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = activityName.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
                Text(text = activityDescription.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 3.dp))
                Text(text = start.toString(), style =  MaterialTheme.typography.labelSmall)
                Text(text = end.toString(), style =  MaterialTheme.typography.labelSmall)
            }
        }
    }

    /*@Composable
    private fun Build*/
}