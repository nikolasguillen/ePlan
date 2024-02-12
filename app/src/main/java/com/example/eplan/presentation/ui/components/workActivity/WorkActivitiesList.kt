package com.example.eplan.presentation.ui.components.workActivity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.Intervention
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.animations.AnimationEmptyList
import com.example.eplan.presentation.ui.components.placeholders.PlaceholderCard
import com.example.eplan.presentation.util.spacing

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun WorkActivitiesList(
    workActivities: List<WorkActivity>,
    onNavigateToActivityDetailScreen: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isConnectionAvailable: Boolean,
    isListCollapsed: Boolean
) {

    val groupedActivities = workActivities.groupBy { it.start.hour }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.small),
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
                            stickyHeader {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colorScheme.surface)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$hour:00",
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(
                                            top = MaterialTheme.spacing.medium,
                                            bottom = MaterialTheme.spacing.small
                                        )
                                    )
                                }
                            }

                            /* Devo usare due diverse liste di items perché se ne uso una sola passando 'isListCollapsed' a 'isCompact', al momento del passaggio da visualizzazione compatta
                            * ad espansa le card che rimangono fuori dalla schermata non si espandono correttamente, rovinando la visualizzazione della lista. */

                            if (isListCollapsed) {
                                itemsIndexed(
                                    items = workActivities,
                                    key = { _, workActivity -> workActivity.id },
                                    contentType = { _, workActivity -> workActivity::class.simpleName.toString() }
                                ) { index, workActivity ->
                                    WorkActivityCard(
                                        workActivity = workActivity,
                                        onClick = {
                                            onClick(
                                                workActivity = workActivity,
                                                onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                            )
                                        },
                                        isCompact = true,
                                        isEven = index % 2 == 0
                                    )
                                }
                            } else {
                                itemsIndexed(
                                    items = workActivities,
                                    key = { _, workActivity -> workActivity.id },
                                    contentType = { _, workActivity -> workActivity::class.simpleName.toString() }
                                ) { index, workActivity ->
                                    WorkActivityCard(
                                        workActivity = workActivity,
                                        onClick = {
                                            onClick(
                                                workActivity = workActivity,
                                                onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                            )
                                        },
                                        isCompact = false,
                                        isEven = index % 2 == 0
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!isConnectionAvailable && !isRefreshing) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    imageVector = Icons.Filled.WifiOff,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.8F
                        )
                    ),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = stringResource(id = R.string.connessione_assente),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
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