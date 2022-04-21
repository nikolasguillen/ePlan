package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen

@Composable
fun ActivitiesList(
    workActivities: List<WorkActivity>,
    onNavigateToActivityDetailScreen: (String) -> Unit
) {

    LazyColumn {
        items(workActivities) { workActivity ->
            ActivityCard(
                workActivity = workActivity,
                onClick = {
                    val route = Screen.WorkActivityDetails.route + "/activityId=${workActivity.id}"
                    onNavigateToActivityDetailScreen(route)
                }
            )
        }
    }
}