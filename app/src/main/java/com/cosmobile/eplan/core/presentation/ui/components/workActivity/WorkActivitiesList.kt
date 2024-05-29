package com.cosmobile.eplan.core.presentation.ui.components.workActivity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.Gap
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.domain.model.WorkActivity
import com.cosmobile.eplan.core.presentation.navigation.NavArguments
import com.cosmobile.eplan.core.presentation.navigation.Screen
import com.cosmobile.eplan.core.presentation.ui.components.animations.AnimationEmptyList
import com.cosmobile.eplan.core.presentation.ui.components.placeholders.AbsentConnectionPlaceholder
import com.cosmobile.eplan.core.presentation.ui.components.placeholders.PlaceholderCard
import com.cosmobile.eplan.core.util.bottomNavPadding
import com.cosmobile.eplan.core.util.spacing
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun WorkActivitiesList(
    workActivities: Map<String, List<WorkActivity>>,
    onNavigateToActivityDetailScreen: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    showAbsentConnectionScreen: Boolean,
    isListCollapsed: Boolean,
    isDayCompleted: Boolean,
    onGapClick: ((date: LocalDate, startTime: LocalTime, endTime: LocalTime) -> Unit)? = null
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                top = MaterialTheme.spacing.small,
                bottom = bottomNavPadding
            ),
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
                    if (workActivities.all { it.value.isEmpty() }) {
                        item {
                            AnimationEmptyList(label = stringResource(id = R.string.no_interventi))
                        }
                    } else {
                        if (isDayCompleted) {
                            item {
                                CompletedDayCard()
                            }
                        }
                        workActivities.forEach { (header, workActivities) ->
                            if (workActivities.isNotEmpty()) {
                                stickyHeader {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .background(color = MaterialTheme.colorScheme.surface)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = header,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.padding(
                                                top = MaterialTheme.spacing.medium,
                                                bottom = MaterialTheme.spacing.small
                                            )
                                        )
                                    }
                                }
                            }


                            /* Devo usare due diverse liste di items perché se ne uso una sola passando 'isListCollapsed' a 'isCompact', al momento del passaggio da visualizzazione compatta
                            * ad espansa le card che rimangono fuori dalla schermata non si espandono correttamente, rovinando la visualizzazione della lista. */

                            if (isListCollapsed) {
                                items(
                                    items = workActivities,
                                    key = { workActivity -> workActivity.id }
                                ) { workActivity ->
                                    when (workActivity) {
                                        is Gap -> {
                                            GapButton(
                                                startTime = workActivity.start.format(
                                                    DateTimeFormatter.ofPattern("HH:mm")
                                                ),
                                                endTime = workActivity.end.format(
                                                    DateTimeFormatter.ofPattern("HH:mm")
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(40.dp)
                                                    .clickable {
                                                        onGapClick?.invoke(
                                                            workActivity.date,
                                                            workActivity.start,
                                                            workActivity.end
                                                        )
                                                    }
                                            )
                                        }

                                        else -> {
                                            WorkActivityCard(
                                                workActivity = workActivity,
                                                onClick = {
                                                    onClick(
                                                        workActivity = workActivity,
                                                        onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                                    )
                                                },
                                                isCompact = true
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(
                                    items = workActivities,
                                    key = { workActivity -> workActivity.id }
                                ) { workActivity ->
                                    when (workActivity) {
                                        is Gap -> {
                                            GapButton(
                                                startTime = workActivity.start.format(
                                                    DateTimeFormatter.ofPattern("HH:mm")
                                                ),
                                                endTime = workActivity.end.format(
                                                    DateTimeFormatter.ofPattern("HH:mm")
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(40.dp)
                                                    .clickable {
                                                        onGapClick?.invoke(
                                                            workActivity.date,
                                                            workActivity.start,
                                                            workActivity.end
                                                        )
                                                    }
                                            )
                                        }

                                        else -> {
                                            WorkActivityCard(
                                                workActivity = workActivity,
                                                onClick = {
                                                    onClick(
                                                        workActivity = workActivity,
                                                        onNavigateToActivityDetailScreen = onNavigateToActivityDetailScreen
                                                    )
                                                },
                                                isCompact = false
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAbsentConnectionScreen && !isRefreshing) {
            AbsentConnectionPlaceholder(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(
                        enabled = true,
                        onClick = {},
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    )
            )
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
                Screen.InterventionDetails.route + "/?${NavArguments.INTERVENTION_ID}=${workActivity.id}"
            onNavigateToActivityDetailScreen(route)
        }

        is Appointment -> {
            val route =
                Screen.AppointmentDetails.route + "/?${NavArguments.APPOINTMENT_ID}=${workActivity.id}"
            onNavigateToActivityDetailScreen(route)
        }
    }
}