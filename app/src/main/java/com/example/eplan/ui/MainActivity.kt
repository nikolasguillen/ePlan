package com.example.eplan.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*


class MainActivity : AppCompatActivity() {

    var selectedDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))*/

        setContent { PaperContent() }


        /*var day: Day?
        val collapsibleCalendar = findViewById<CollapsibleCalendar>(R.id.calendarView)
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onClickListener() {
            }

            override fun onDataUpdate() {
            }

            override fun onDayChanged() {
            }

            override fun onDaySelect() {
                day = collapsibleCalendar.selectedDay
                collapsibleCalendar.collapse(100)
            }

            override fun onItemClick(v: View) {
            }

            override fun onMonthChange() {
            }

            override fun onWeekChange(position: Int) {
            }

        })*/
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaperContent() {
        Scaffold(topBar = {TopBar()},
            bottomBar = {BottomNavBar()}) {
        }
    }

    @Composable
    private fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
    }

    @Composable
    private fun BottomNavBar() {
        NavigationBar(containerColor = colorResource(id = R.color.primaryLightColor),
            contentColor = colorResource(id = R.color.black),
            content = {})
    }
}