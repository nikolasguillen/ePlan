package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ActivitiesList(
    workActivities: List<WorkActivity>,
    onNavigateToActivityDetailScreen: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        if (isRefreshing) {
            LazyColumn {
                items(count = 3) {
                    PlaceholderCard()
                }
            }
        } else {
            if (workActivities.isEmpty()) {
                PlaceholderEmptyList(stringResource(id = R.string.no_interventi))
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(workActivities) { workActivity ->
                        ActivityCard(
                            workActivity = workActivity,
                            onClick = {
                                val route =
                                    Screen.WorkActivityDetails.route + "/activityId=${workActivity.id}"
                                onNavigateToActivityDetailScreen(route)
                            }
                        )
                    }
                }
            }
        }
    }
}