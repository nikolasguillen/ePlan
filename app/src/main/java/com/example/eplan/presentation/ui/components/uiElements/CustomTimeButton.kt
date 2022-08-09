package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.eplan.presentation.ui.components.uiElements.TimePicker
import com.example.eplan.presentation.util.spacing
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@ExperimentalMaterial3Api
@Composable
fun CustomTimeButton(
    time: String,
    label: String,
    onClick: (LocalTime) -> Unit,
    modifier: Modifier
) {

    val dialogState = rememberMaterialDialogState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable { dialogState.show() }
        )
        {
            Text(
                text = "$label: $time",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
    TimePicker(startTime = time, dialogState = dialogState, onClick = onClick)
}