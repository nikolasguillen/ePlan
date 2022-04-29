package com.example.eplan.presentation.ui.workActivityRecord

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.eplan.R
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.util.bottomNavPadding
import com.example.eplan.presentation.util.spacing
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun ActivityRecordScreen(
    onSave: (String) -> Unit,
    viewModel: ActivityRecordViewModel
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.walking_turkey))
    val start = viewModel.start.value
    val end = viewModel.end.value
    val showReset = remember { mutableStateOf(false) }

    Scaffold(
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = bottomNavPadding, top = 150.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = stringResource(R.string.orario_inizio),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = start,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.orario_fine),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = end,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                AnimatedVisibility(
                    visible = viewModel.isRecording(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(size = 200.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (showReset.value) {
                            FloatingActionButton(
                                onClick = { viewModel.reset() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.ripristina)
                                )
                            }
                        }
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        LargeFloatingActionButton(
                            onClick = {
                                if (viewModel.isOver()) {
                                    val route = Screen.WorkActivityDetails.route + "/activityId=null?date=${LocalDate.now()}&start=${start}&end=${end}"
                                    onSave(route)
                                } else {
                                    when (viewModel.isRecording()) {
                                        false -> {
                                            viewModel.setStart(LocalTime.now())
                                            showReset.value = true
                                        }
                                        true -> viewModel.setEnd(LocalTime.now())
                                    }
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                painter =
                                if (viewModel.isOver()) {
                                    painterResource(id = R.drawable.ic_baseline_save_24)
                                } else {
                                    when (viewModel.isRecording()) {
                                        true -> painterResource(id = R.drawable.ic_baseline_stop_24)
                                        false -> painterResource(id = R.drawable.ic_baseline_play_arrow_24)
                                    }
                                },
                                contentDescription = stringResource(R.string.registra)
                            )
                        }
                    }

                    //Placeholder per spaziare bene la riga
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    )
}