package com.example.eplan.presentation.ui.workActivityList

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.TopBar
import com.example.eplan.presentation.ui.components.CollapsibleCalendar
import com.example.eplan.presentation.util.TAG
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesListScreen(viewModel: ActivityListViewModel, onNavigate: (String) -> Unit) {

    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.foglio_ore),
                navigate = { onNavigate(Screen.Account.route) })
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
                            LocalDate.of(year, month, dayOfMonth).format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            )
                        )
                        viewModel.onTriggerEvent(
                            ActivityListEvent.DayChangeEvent
                        )
                    },
                    startDate = LocalDate.parse(
                        viewModel.query.value,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
                )
                ActivitiesList(
                    workActivities = viewModel.workActivities.value,
                    onNavigateToActivityDetailScreen = onNavigate
                )
            }
        }
    )
}