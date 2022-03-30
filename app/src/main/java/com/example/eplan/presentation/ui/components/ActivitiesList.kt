package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import kotlinx.coroutines.launch

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
                    val route = Screen.WorkActivityDetails.route + "/${workActivity.id}"
                    onNavigateToActivityDetailScreen(route)
                }
            )
        }
    }
}