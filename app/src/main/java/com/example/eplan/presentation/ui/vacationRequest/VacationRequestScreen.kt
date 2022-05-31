package com.example.eplan.presentation.ui.vacationRequest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.components.BottomSingleActionBar
import com.example.eplan.presentation.ui.components.CustomDateButton
import com.example.eplan.presentation.util.fromDateToLocalDate
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
@Composable
fun VacationRequestScreen(
    viewModel: VacationRequestViewModel
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text(text = "Richiesta ferie") }, navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            })
        },
        bottomBar = {
            BottomSingleActionBar(
                item = BottomNavBarItems.Send,
                onClick = {}
            )
        }) { paddingValues ->
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
                Text(text = "Tipo di richiesta", style = MaterialTheme.typography.labelLarge)
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
                                            contentDescription = ""
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
                                text = "Giornata",
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
                                            contentDescription = ""
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
                                text = "Periodo",
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
                Text(text = "Seleziona la giornata", style = MaterialTheme.typography.headlineMedium)
                CustomDateButton(
                    date = viewModel.singleDate.value,
                    onDateSelected = { date -> viewModel.singleDate.value = fromDateToLocalDate(date) })
            } else {
                Text(text = "Scegli data inizio", style = MaterialTheme.typography.headlineMedium)
                CustomDateButton(
                    date = viewModel.startDate.value,
                    onDateSelected = { date -> viewModel.startDate.value = fromDateToLocalDate(date) })
                Text(
                    text = "Scegli data fine",
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomDateButton(
                    date = viewModel.endDate.value,
                    onDateSelected = { date -> viewModel.endDate.value = fromDateToLocalDate(date) })
            }
        }
    }
}