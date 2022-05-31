package com.example.eplan.presentation.ui.timeStats

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.eplan.R
import com.example.eplan.presentation.ui.timeStats.TimeStatsEvent.GetStats
import com.example.eplan.presentation.util.spacing
import com.example.eplan.presentation.util.toLiteralDateParser
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun TimeStatsScreen(
    viewModel: TimeStatsViewModel
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
            SmallTopAppBar(
                title = { Text(text = "Statistiche ore") },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onTriggerEvent(GetStats) }) {
                        Icon(
                            imageVector = Icons.Filled.Autorenew,
                            contentDescription = stringResource(id = R.string.aggiorna)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize()
        ) {
            /*ComposeCalendar(listener = object : SelectDateListener {
                override fun onDateSelected(date: Date) {
                    TODO("Not yet implemented")
                }

                override fun onCanceled() {
                    TODO("Not yet implemented")
                }
            },
            themeColor = MaterialTheme.colorScheme.primary)*/

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.stats) {
                    Text(text = "${toLiteralDateParser(it.date.toString())}, ${it.standardTime}, ${it.overtime}, ${it.vacation}, ${it.disease}", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
