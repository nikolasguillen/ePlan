package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing

@ExperimentalMaterial3Api
@Composable
fun MultiFloatingActionButton(
    onAddClick: () -> Unit,
    onRecordClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        SmallFloatingActionButton(
            onClick = { onRecordClick() },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(
                    id = R.string.registra_intervento
                )
            )
        }
        FloatingActionButton(
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