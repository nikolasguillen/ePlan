package com.example.eplan.presentation.ui.workActivityList

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.presentation.navigation.Screen
import com.example.eplan.presentation.ui.components.ActivitiesList
import com.example.eplan.presentation.ui.components.TopBar

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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.aggiungi_attivita)
                )
            }
        },
        content = {
            ActivitiesList(
                workActivities = viewModel.workActivities.value,
                onNavigateToActivityDetailScreen = onNavigate
            )
        }
    )
}