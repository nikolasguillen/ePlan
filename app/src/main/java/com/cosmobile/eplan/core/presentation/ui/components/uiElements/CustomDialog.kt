package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cosmobile.eplan.core.util.spacing
import com.cosmobile.eplan.R

private val DEFAULT = {}

@ExperimentalMaterial3Api
@Composable
fun CustomDialog(
    title: String = "",
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit = DEFAULT,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp.times(0.8F))
        ) {
            Column(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            ) {
                if (title.isNotBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            )
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Column(content = content)
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.annulla))
                    }
                    if (onConfirmationRequest !== DEFAULT) {
                        TextButton(onClick = onConfirmationRequest) {
                            Text(text = stringResource(id = R.string.conferma))
                        }
                    }
                }
            }
        }
    }
}