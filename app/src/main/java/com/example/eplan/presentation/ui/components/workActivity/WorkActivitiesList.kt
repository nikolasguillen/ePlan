package com.example.eplan.presentation.ui.components.workActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.Intervention
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.AnimationEmptyList
import com.example.eplan.presentation.ui.components.placeholders.PlaceholderCard
import com.example.eplan.presentation.util.spacing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalMaterial3Api
@Composable
fun WorkActivitiesList(
    workActivities: List<WorkActivity>,
    onNavigateToActivityDetailScreen: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
        ) {
            when (isRefreshing) {
                true -> {
                    items(3) {
                        PlaceholderCard()
                    }
                }
                false -> {
                    if (workActivities.isEmpty()) {
                        item {
                            AnimationEmptyList(stringResource(id = R.string.no_interventi))
                        }
                    } else {
                        items(workActivities) { workActivity ->
                            WorkActivityCard(
                                workActivity = workActivity,
                                onClick = {
                                    onClick(
                                        workActivity = workActivity,
                                        onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun onClick(
    workActivity: WorkActivity,
    onNavigateToActivityDetailScreen: (String) -> Unit
) {
    when (workActivity) {
        is Intervention -> {
            val route =
                Screen.InterventionDetails.route + "/?activityId=${workActivity.id}"
            onNavigateToActivityDetailScreen(route)
        }
        is Appointment -> {
            val route =
                Screen.AppointmentDetails.route + "/?appointmentId=${workActivity.id}"
            onNavigateToActivityDetailScreen(route)
        }
    }
}