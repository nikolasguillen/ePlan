package com.example.eplan.presentation.ui.workActivityRecord

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.eplan.R
import com.example.eplan.presentation.util.bottomNavPadding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ActivityRecordScreen(
) {
    val recording = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.walking_turkey))

    // TODO questa roba sarà da mettere nella viewmodel, non direttamente nel composable
    val start = remember {
        mutableStateOf(
            LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
        )
    }
    val end = remember { mutableStateOf(LocalTime.now()) }

    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavPadding),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            LargeFloatingActionButton(onClick = { recording.value = !recording.value }) {
                if (recording.value) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_stop_24),
                        contentDescription = "Avvia registrazione"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Avvia registrazione"
                    )
                    // TODO sistemare ripetizione ridondante
                }
            }
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
            ) {
                Row() {
                    Column {
                        Text(text = "Orario inizio")
                        Text(text = if (!recording.value) "--:--" else start.value.toString())
                    }
                }
                AnimatedVisibility(
                    visible = recording.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(
                            width = 200.dp, height = 200.dp
                        )
                    )
                }
            }
        }
    )
}