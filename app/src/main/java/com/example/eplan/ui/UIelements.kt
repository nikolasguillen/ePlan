package com.example.eplan.ui

import com.example.eplan.R
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eplan.HomeFragmentDirections
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

private val ourDarkColorScheme = darkColorScheme()

private val ourLightColorScheme = lightColorScheme()

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val ourColorScheme = if (darkTheme) ourDarkColorScheme else ourLightColorScheme

    MaterialTheme(
        content = content,
        colorScheme = ourColorScheme
    )
}

@Composable
fun TopBar(title: String) {
    SmallTopAppBar(title = { Text(text = title) })
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Appointments,
        NavigationItem.Account
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route)
                    }
                },
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
fun ActivityCard(
    activityName: String,
    activityDescription: String,
    start: String,
    end: String,
    oreSpostamento: String,
    km: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                       navController.navigate("activityDetails/${activityName}/${activityDescription}/{ciao}/${start}/${end}/${oreSpostamento}/${km}/{true}")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activityName.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = activityDescription.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(text = start, style = MaterialTheme.typography.labelSmall)
            Text(text = end, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SetupCalendar() {

    var day: Day? = null

    val todayBackgroundColor = MaterialTheme.colorScheme.primary.toArgb()
    val todayTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val selectedDayBackgroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val selectedDayTextColor = MaterialTheme.colorScheme.surface.toArgb()
    val dynamicTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                textColor = dynamicTextColor
                setExpandIconColor(dynamicTextColor)

                // Material dynamic theme
                AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                    ?.setTint(selectedDayBackgroundColor)
                selectedItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                selectedItemTextColor = selectedDayTextColor
                AppCompatResources.getDrawable(context, R.drawable.today_circle)
                    ?.setTint(todayBackgroundColor)
                todayItemBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.today_circle)
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