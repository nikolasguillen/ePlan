package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.presentation.ui.items.ActivityCard
import com.example.eplan.presentation.ui.items.BottomNavBar
import com.example.eplan.presentation.ui.items.SetupCalendar
import com.example.eplan.presentation.ui.items.TopBar
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: ActivityListViewModel) {

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = { TopBar(stringResource(R.string.foglio_ore), navController = navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
        },
        content = {

            val workActivities = viewModel.workActivities.value

            Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                SetupCalendar()
                LazyColumn {
                    items(workActivities) { workActivity ->
                        ActivityCard(
                            workActivity = workActivity,
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}