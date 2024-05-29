package com.cosmobile.eplan.core.presentation.ui.components.placeholders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.spacing

@Composable
fun AbsentConnectionPlaceholder(
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            imageVector = Icons.Filled.WifiOff,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.8F
                )
            ),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = stringResource(id = R.string.connessione_assente),
            style = MaterialTheme.typography.bodyLarge
        )
        onRetry?.let {
            TextButton(onClick = it) {
                Text(text = stringResource(id = R.string.tocca_per_riprovare))
            }
        }
    }
}