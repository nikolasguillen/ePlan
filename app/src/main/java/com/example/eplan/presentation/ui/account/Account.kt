package com.example.eplan.presentation.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

const val name = "Nikolas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Ciao $name!") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "go back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = it.calculateBottomPadding()
                )
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        Text(
                            text = "Profilo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                    item {
                        Text(
                            text = "Impostazioni",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                    item {
                        Text(
                            text = "Informazioni sull'app",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                }
            }
        }
    )
}