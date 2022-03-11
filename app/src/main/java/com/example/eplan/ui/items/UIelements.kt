package com.example.eplan.ui.items

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eplan.model.Appointment
import com.example.eplan.model.WorkActivity
import com.example.eplan.model.toJson
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = if (dynamic) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context = context) else dynamicLightColorScheme(
            context = context
        )
    } else {
        if (darkTheme) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        content = content,
        colorScheme = colorScheme
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
    workActivity: WorkActivity,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                val argument = workActivity.toJson()
                navController.navigate("activityDetails/$argument")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workActivity.activityName.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = workActivity.activityDescription.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(text = workActivity.start.toString(), style = MaterialTheme.typography.labelSmall)
            Text(text = workActivity.end.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                val argument = appointment.toJson()
                navController.navigate("activityDetails/$argument")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = appointment.activity.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appointment.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(text = appointment.start, style = MaterialTheme.typography.labelSmall)
            Text(text = appointment.end, style = MaterialTheme.typography.labelSmall)
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

@Composable
fun CustomTextField(
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

fun customTimePicker(time: MutableState<String>, context: Context) {

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