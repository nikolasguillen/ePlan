package com.example.eplan.presentation.ui.vacationRequest

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.BottomNavBarItem
import com.example.eplan.presentation.navigation.BottomNavbarAction
import com.example.eplan.presentation.ui.components.uiElements.BottomActionBar
import com.example.eplan.presentation.util.spacing
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@ExperimentalMaterial3Api
@Composable
fun VacationRequestScreen(
    viewModel: VacationRequestViewModel,
    onRequestSent: (startDate: Long?, endDate: Long?) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val datePickerState = rememberDateRangePickerState(
        selectableDates = object : SelectableDates {
            // Blocks Sunday and Saturday from being selected.
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
                    .toLocalDate().dayOfWeek
                return dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY
            }

            // Allow selecting dates from year 2023 forward.
            override fun isSelectableYear(year: Int): Boolean {
                return year >= LocalDate.now().year
            }
        }
    )

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { error ->
            snackBarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.richiesta_ferie)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.indietro
                            )
                        )
                    }
                })
        },
        bottomBar = {
            BottomActionBar(
                actions = listOf(
                    BottomNavbarAction(
                        item = BottomNavBarItem.Send,
                        onClick = {
                            onRequestSent(
                                datePickerState.selectedStartDateMillis,
                                datePickerState.selectedEndDateMillis
                            )
                        }
                    )
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        datePickerState.selectedStartDateMillis
        BackHandler {
            onBackPressed()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        ) {

            DateRangePicker(
                state = datePickerState,
                showModeToggle = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}