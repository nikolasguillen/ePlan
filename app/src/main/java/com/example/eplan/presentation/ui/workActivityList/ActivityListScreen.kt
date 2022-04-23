package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.*
import com.example.eplan.presentation.util.bottomNavPadding
import java.time.LocalDate


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit
) {

    val workActivities = viewModel.workActivities

    val date = viewModel.date.value

    val loading = viewModel.loading.value

    val calendarVisibility = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(LocalDate.parse(date)) }

    val isExpanded = remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_box))

    LaunchedEffect(key1 = true) {
        viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
    }


    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavPadding),
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        floatingActionButton = {
            MultiFloatingActionButton(
                onAddClick = {
                    isExpanded.value = !isExpanded.value
                    val id = "null"
                    val route = Screen.WorkActivityDetails.route + "/activityId=${id}?date=${date}"
                    onNavigate(route)
                },
                onExpandClick = {
                    isExpanded.value = !isExpanded.value
                },
                onRecordClick = {
                    onNavigate(Screen.WorkActivityRecord.route)
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                // Riga con data che al click apre/chiude il calendario
                CollapsibleCalendar(
                    calendarVisibility = calendarVisibility,
                    date = date,
                    onDayChange = { date ->
                        selectedDate.value = LocalDate.parse(date)
                        viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
                        calendarVisibility.value = false
                    }
                )

                if (loading) {
                    repeat(3) {
                        PlaceholderCard()
                    }
                } else {
                    if (workActivities.value.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(size = 200.dp)
                                    .padding(all = 16.dp)
                            )
                            Text(
                                text = stringResource(R.string.no_interventi),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        ActivitiesList(
                            workActivities = workActivities.value,
                            onNavigateToActivityDetailScreen = onNavigate
                        )
                    }
                }
            }

            // Ombra per quando apro il multi action FAB
            AnimatedVisibility(visible = isExpanded.value, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5F))
                        .clickable(enabled = false, onClick = {})
                        .fillMaxSize()
                )
            }
        }
    )
}