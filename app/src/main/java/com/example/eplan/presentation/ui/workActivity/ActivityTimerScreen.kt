package com.example.eplan.presentation.ui.workActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import java.time.Duration
import java.time.LocalTime

@Composable
@Preview
fun ActivityTimerScreen(
) {
    val startTime = LocalTime.now()
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = Duration.between(startTime, LocalTime.now()).toString())
    }
}