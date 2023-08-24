package com.example.eplan.presentation.ui.components.workActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.Intervention
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.animations.AnimationEmptyList
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

    val groupedActivities = workActivities.groupBy { it.start.hour }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium)
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
                        groupedActivities.forEach { (hour, workActivities) ->
                            item {
                                Text(
                                    text = "$hour:00",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                            }
                            items(workActivities) { workActivity ->
                                if (workActivities.indexOf(workActivity) == 0) {
                                    Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))
                                }
                                WorkActivityCard(
                                    workActivity = workActivity,
                                    onClick = {
                                        onClick(
                                            workActivity = workActivity,
                                            onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                        )
                                    }
                                )
                                if (workActivities.indexOf(workActivity) == workActivities.lastIndex) {
                                    Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))
                                }
                            }
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