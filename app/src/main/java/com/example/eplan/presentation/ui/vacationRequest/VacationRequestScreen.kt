package com.example.eplan.presentation.ui.vacationRequest

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.components.BottomSingleActionBar
import com.example.eplan.presentation.ui.components.CustomDateButton
import com.example.eplan.presentation.ui.vacationRequest.VacationRequestEvent.RequestEvent
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.spacing
import kotlinx.coroutines.flow.collect

@ExperimentalMaterial3Api
@Composable
fun VacationRequestScreen(
    viewModel: VacationRequestViewModel,
    onRequestSent: () -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.indietro
                            )
                        )
                    }
                })
        },
        bottomBar = {
            BottomSingleActionBar(
                item = BottomNavBarItems.Send,
                onClick = {
                    onRequestSent()
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.tipo_richiesta),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-1).dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = if (viewModel.singleDayVacancy) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStartPercent = 50,
                            bottomStartPercent = 50,
                            topEndPercent = 0,
                            bottomEndPercent = 0
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    bottomStartPercent = 50,
                                    topEndPercent = 0,
                                    bottomEndPercent = 0
                                )
                            )
                            .clickable {
                                viewModel.singleDayVacancy = true
                            }
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .height(MaterialTheme.typography.labelLarge.lineHeight.value.dp)
                        ) {
                            Crossfade(targetState = viewModel.singleDayVacancy) {
                                if (it) {
                                    Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth)) {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = stringResource(id = R.string.giornata)
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth))
                                }
                            }
                            Spacer(
                                modifier = Modifier.width(MaterialTheme.spacing.extraSmall)
                            )
                            Text(
                                text = stringResource(R.string.giornata),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(
                                modifier = Modifier.width(MaterialTheme.spacing.extraSmall)
                            )
                            Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth))
                        }
                    }
                    Surface(
                        color = if (!viewModel.singleDayVacancy) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStartPercent = 0,
                            bottomStartPercent = 0,
                            topEndPercent = 50,
                            bottomEndPercent = 50
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 0,
                                    bottomStartPercent = 0,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .clickable {
                                viewModel.singleDayVacancy = false
                            }
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .height(MaterialTheme.typography.labelLarge.lineHeight.value.dp)
                        ) {
                            Crossfade(targetState = !viewModel.singleDayVacancy) {
                                if (it) {
                                    Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth)) {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = stringResource(id = R.string.periodo)
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth))
                                }
                            }
                            Spacer(
                                modifier = Modifier.width(MaterialTheme.spacing.extraSmall)
                            )
                            Text(
                                text = stringResource(R.string.periodo),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(
                                modifier = Modifier.width(MaterialTheme.spacing.extraSmall)
                            )
                            Box(modifier = Modifier.size(Icons.Filled.Done.defaultWidth))
                        }
                    }
                }
            }
            if (viewModel.singleDayVacancy) {
                Text(
                    text = stringResource(R.string.seleziona_giornata),
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomDateButton(
                    date = viewModel.singleDate.value,
                    onDateSelected = { date ->
                        viewModel.singleDate.value = fromDateToLocalDate(date)
                    })
            } else {
                Text(
                    text = stringResource(R.string.scegli_data_inizio),
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomDateButton(
                    date = viewModel.startDate.value,
                    onDateSelected = { date ->
                        viewModel.startDate.value = fromDateToLocalDate(date)
                    })
                Text(
                    text = stringResource(R.string.scegli_data_fine),
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomDateButton(
                    date = viewModel.endDate.value,
                    onDateSelected = { date ->
                        viewModel.endDate.value = fromDateToLocalDate(date)
                    })
            }
        }
    }
}