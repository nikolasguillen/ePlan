package com.example.eplan.ui

import android.graphics.BlendMode
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.R
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

var day: Day? = null
var selectedDay: Int = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { HomeBars() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeBars() {
        Scaffold(bottomBar = { BottomNavBar() },
            topBar = { TopBar() },
            content = { setupCalendar() })
    }

    @Composable
    @Preview
    private fun setupCalendar() {

        AndroidView(factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = R.color.primaryColor
                textColor = R.color.white

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
        })
    }

    @Composable
    private fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) }, )
    }

    @Composable
    private fun BottomNavBar() {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Appointments,
            NavigationItem.Account
        )
        NavigationBar(
            containerColor = colorResource(id = R.color.primaryColor),
            contentColor = colorResource(id = R.color.black)) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = (item.title == "Foglio ore"),
                    onClick = {},
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title)},
                    label = { Text(text = item.title)}
                    )
            }
        }
    }
}