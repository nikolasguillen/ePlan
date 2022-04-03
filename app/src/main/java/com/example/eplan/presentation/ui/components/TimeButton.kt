package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@ExperimentalMaterial3Api
@Composable
fun CustomTimeButton(time: String, label: String, onClick: (String) -> Unit) {

    val dialogState = rememberMaterialDialogState()

    Card(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable { dialogState.show() }
    )
    {
        Text(
            text = "$label: $time",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
    CustomTimePicker(startTime = time, dialogState = dialogState, onClick = onClick)
}