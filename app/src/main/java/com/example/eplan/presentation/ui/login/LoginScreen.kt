package com.example.eplan.presentation.ui.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.util.TAG


@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginAttempted: () -> Unit
) {

    val error = remember { viewModel.successfulLoginAttempt }
    val loading = remember {
        viewModel.loading
    }

    Scaffold {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!error.value) {
                    Text(text = viewModel.message.value, color = Color.Red)
                }
                OutlinedTextField(
                    value = viewModel.username.value,
                    onValueChange = { viewModel.username.value = it })

                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.password.value = it })

                Button(onClick = {
                    onLoginAttempted()
                }) {
                    Text(text = stringResource(R.string.login))
                }
            }
        }
        if (loading.value) {
            Dialog(onDismissRequest = { loading.value = false }) {
                CircularProgressIndicator()
            }
        }
    }
}