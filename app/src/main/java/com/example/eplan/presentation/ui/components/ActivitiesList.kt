package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.items.SetupCalendar

@Composable
fun ActivitiesList(
    workActivities: List<WorkActivity>,
    onNavigateToActivityDetailScreen: (String) -> Unit
) {

    Column {
        SetupCalendar()
        LazyColumn {
            items(workActivities) { workActivity ->
                ActivityCard(
                    workActivity = workActivity,
                    onClick = {
                        val route = Screen.WorkActivityDetails.route + "/${workActivity.id}"
                        onNavigateToActivityDetailScreen(route)
                    }
                )
            }
        }
    }
}