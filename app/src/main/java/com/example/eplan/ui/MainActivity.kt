package com.example.eplan.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eplan.R
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*


class MainActivity : AppCompatActivity() {

    var selectedDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        /* starts before 1 month from now *//*
        *//* starts before 1 month from now *//*
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        *//* ends after 1 month from now *//*

        *//* ends after 1 month from now *//*
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .build()

        var day: Int?
        day = java.util.Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                day = date?.get(Calendar.DAY_OF_MONTH)
            }
        }*/
        var day: Day?
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

        })
    }
}