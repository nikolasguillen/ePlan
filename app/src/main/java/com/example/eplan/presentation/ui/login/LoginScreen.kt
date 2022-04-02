package com.example.eplan.presentation.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eplan.R


@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginAttempted: (username: String, password: String) -> Unit
) {
    Scaffold {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.username.value,
                onValueChange = { viewModel.username.value = it })

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.password.value = it })

            Button(onClick = {
                onLoginAttempted(viewModel.username.value, viewModel.password.value)
            }) {
                Text(text = stringResource(R.string.login))
            }
        }
    }
}