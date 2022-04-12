package com.example.eplan.presentation.ui.workActivityList

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.*
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.bottomNavPadding
import com.example.eplan.presentation.util.toLiteralDateParser
import java.time.LocalDate


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit,
    selectedDate: MutableState<LocalDate>
) {

    val workActivities = viewModel.workActivities

    val date = viewModel.date.value

    val loading = viewModel.loading.value

    val calendarVisibility = remember { mutableStateOf(false) }

    val isExpanded = remember {
        mutableStateOf(false)
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
                    onAddClick()
                },
                onExpandClick = {
                    isExpanded.value = !isExpanded.value
                }
            )
        },
        content = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { calendarVisibility.value = !calendarVisibility.value }
                        .padding(16.dp)
                ) {
                    Text(
                        text = toLiteralDateParser(date = date),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Crossfade(targetState = calendarVisibility.value) { isVisible ->
                        if (isVisible) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = "Chiudi il calendario"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Apri il calendario"
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = calendarVisibility.value) {
                    CollapsibleCalendar(
                        date = date,
                        onDayChange = { date ->
                            selectedDate.value = LocalDate.parse(date)
                            viewModel.onTriggerEvent(ActivityListEvent.DayChangeEvent(date = date))
                            calendarVisibility.value = false
                        }
                    )
                }
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
                            GifImage(
                                imageID = R.drawable.travolta,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 64.dp)
                            )
                            Text(
                                text = "Non ci sono interventi da mostrare",
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