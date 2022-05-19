package com.example.eplan.presentation.ui.vacationRequest

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eplan.presentation.navigation.BottomNavBarItems
import com.example.eplan.presentation.ui.components.BottomSingleActionBar
import com.example.eplan.presentation.util.spacing
import java.time.Instant
import java.util.*

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
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Row() {
                Text(
                    text = "Scegli la data iniziale",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            AndroidView(
                modifier = Modifier.fillMaxWidth(), factory = {
                    CalendarView(it).apply {
                        this.date = Date.from(Instant.now()).time
                    }
                })
            Row() {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Conferma data selezionata")
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium)
            ) {
                AnimatedVisibility(
                    visible = viewModel.startDate.value != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column() {
                        Text(text = "Dal", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                AnimatedVisibility(
                    visible = viewModel.endDate.value != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column() {
                        Text(text = "Al", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}