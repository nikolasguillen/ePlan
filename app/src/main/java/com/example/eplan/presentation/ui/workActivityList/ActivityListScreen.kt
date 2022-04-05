package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.eplan.R
import com.example.eplan.presentation.navigation.NestedNavGraphs
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.ui.components.TopBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun ActivitiesListScreen(
    viewModel: ActivityListViewModel,
    onNavigate: (String) -> Unit,
    bottomPadding: Dp
) {

    val workActivities = viewModel.workActivities.value

    val query = viewModel.query.value

    val loading = viewModel.loading.value

    Scaffold(
        modifier = Modifier.padding(bottom = bottomPadding),
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(NestedNavGraphs.AccountGraph.route) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.aggiungi_attivita)
                )
            }
        },
        content = {
            Column {
                CollapsibleCalendar(
                    onDaySelected = { dayOfMonth, month, year ->

                        viewModel.onQueryChanged(
                            LocalDate.of(year, month, dayOfMonth).toString()
                        )
                        viewModel.onTriggerEvent(
                            ActivityListEvent.DayChangeEvent
                        )
                    },
                    startDate = LocalDate.parse(query, DateTimeFormatter.ISO_DATE)
                )
                ActivitiesList(
                    workActivities = workActivities,
                    onNavigateToActivityDetailScreen = onNavigate,
                    loading = loading
                )
            }
        }
    )
}