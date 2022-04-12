package com.example.eplan.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiFloatingActionButton(
    onExpandClick: () -> Unit,
    onAddClick: () -> Unit,
    onRecordClick: () -> Unit
) {

    val isExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = isExpanded.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card() {
                        Text(
                            text = "Registra intervento",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(all = 6.dp)
                        )
                    }
                    SmallFloatingActionButton(
                        onClick = { onRecordClick() },
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card() {
                        Text(
                            text = "Inserisci manualmente",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(all = 6.dp)
                        )
                    }
                    SmallFloatingActionButton(
                        onClick = { onAddClick() },
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(imageVector = Icons.Filled.Create, contentDescription = "")
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                isExpanded.value = !isExpanded.value
                onExpandClick()
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            val transition =
                updateTransition(targetState = isExpanded.value, label = "Add button rotation")
            val rotation: Float by transition.animateFloat(label = "Add button rotation") { state ->
                if (state) 45F else 0F
            }
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.aggiungi_attivita),
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}