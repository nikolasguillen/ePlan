package com.example.eplan.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.util.toJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable {
                val argument = appointment.toJson()
                navController.navigate("appointmentDetails/$argument")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = appointment.activity.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appointment.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(text = appointment.start, style = MaterialTheme.typography.labelSmall)
            Text(text = appointment.end, style = MaterialTheme.typography.labelSmall)
        }
    }
}