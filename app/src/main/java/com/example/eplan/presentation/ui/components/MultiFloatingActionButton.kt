package com.example.eplan.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing

@ExperimentalMaterial3Api
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
                    .padding(bottom = MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card {
                        Text(
                            text = stringResource(R.string.registra_intervento),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(all = MaterialTheme.spacing.small)
                        )
                    }
                    SmallFloatingActionButton(
                        onClick = { onRecordClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(
                                id = R.string.registra_intervento
                            )
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card {
                        Text(
                            text = stringResource(R.string.inserisci_manualmente),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(all = MaterialTheme.spacing.small)
                        )
                    }
                    SmallFloatingActionButton(
                        onClick = { onAddClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = stringResource(
                                id = R.string.inserisci_manualmente
                            )
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                isExpanded.value = !isExpanded.value
                onExpandClick()
            }
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