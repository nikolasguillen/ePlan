package com.example.eplan.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.eplan.R
import com.example.eplan.domain.model.Activity
import com.example.eplan.presentation.util.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySelectorScreen(
    activities: List<Activity?>,
    selectedActivityId: String?,
    onActivitySelected: (String) -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(id = R.string.seleziona_attivita)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.indietro)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        BackHandler { onBackPressed() }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onQueryChange(it) },
                label = { Text(text = stringResource(id = R.string.attivita)) },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                activities.filter {
                    it?.name?.contains(searchQuery, true) == true
                }.forEach { activity ->
                    activity?.let {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .clickable { onActivitySelected(it.id) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(selected = it.id == selectedActivityId, onClick = { onActivitySelected(it.id) })
                                Text(text = it.name)
                            }
                        }
                    }
                }
            }
        }
    }
}