package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen

@Composable
fun ActivitiesList(
    workActivities: MutableState<List<WorkActivity>>,
    onNavigateToActivityDetailScreen: (String) -> Unit,
    loading: Boolean
) {

    LazyColumn {
        items(workActivities.value) { workActivity ->
            ActivityCard(
                workActivity = workActivity,
                onClick = {
                    val route = Screen.WorkActivityDetails.route + "/${workActivity.id}"
                    onNavigateToActivityDetailScreen(route)
                },
                loading = loading
            )
        }
    }
}