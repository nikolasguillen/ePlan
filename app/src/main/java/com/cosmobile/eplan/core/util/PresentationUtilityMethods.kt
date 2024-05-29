package com.cosmobile.eplan.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cosmobile.eplan.core.presentation.navigation.NestedNavGraphs
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun fromLocalDateToDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun fromDateToLocalDate(date: Date): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun fromLocalTimeToString(localTime: LocalTime): String {
    return String.format("%02d", localTime.hour) + ":" + String.format("%02d", localTime.minute)
}

fun fromStringToLocalTime(timeString: String): LocalTime {
    val hour = timeString.split(":")[0].toInt()
    val minute = timeString.split(":")[1].toInt()

    return LocalTime.of(hour, minute)
}

fun acceptableTimeInterval(start: LocalTime?, end: LocalTime?): Boolean {
    return if (start != null && end != null) {
        Duration.between(start, end).toMinutes() > 0
    } else {
        false
    }
}

fun toLiteralDateParser(date: LocalDate): String {
    val headerDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    return headerDate.split(" ")[0] +
            " " +
            headerDate.split(" ")[1].replaceFirstChar { it.uppercase() } +
            " " +
            headerDate.split(" ")[2]
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

fun NavHostController.popupToLogin() {
    this.popBackStack(
        route = NestedNavGraphs.AccountGraph.startDestination,
        inclusive = true
    )
    this.popBackStack(
        route = NestedNavGraphs.AppointmentGraph.startDestination,
        inclusive = true
    )
    this.popBackStack(
        route = NestedNavGraphs.InterventionGraph.startDestination,
        inclusive = true
    )
    this.popBackStack(
        route = NestedNavGraphs.LoginGraph.startDestination,
        inclusive = true
    )
    this.navigate(route = NestedNavGraphs.LoginGraph.startDestination)
}