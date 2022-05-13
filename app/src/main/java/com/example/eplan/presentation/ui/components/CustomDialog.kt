package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing

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