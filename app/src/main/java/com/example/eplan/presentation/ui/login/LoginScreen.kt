package com.example.eplan.presentation.ui.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing


@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginAttempted: () -> Unit,
    onSuccessfulLogin: () -> Unit
) {

    val successfulLogin by viewModel.successfulLoginAttempt.collectAsState()
    val loading = viewModel.loading
    val activity = LocalContext.current as Activity
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.successfulLoginAttempt.collect {
            if (it) onSuccessfulLogin()
        }
    }

    Scaffold { paddingValues ->
        BackHandler(enabled = true) {
            activity.finish()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .fillMaxWidth()
            ) {
                if (!successfulLogin) {
                    Text(text = viewModel.message.value, color = Color.Red)
                }
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
                    OutlinedTextField(
                        value = viewModel.username.value,
                        onValueChange = { viewModel.username.value = it },
                        placeholder = { Text(text = stringResource(R.string.username)) },
                        isError = viewModel.usernameError.value != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.usernameError.value != null) {
                        Text(
                            text = stringResource(R.string.campo_vuoto),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
                    OutlinedTextField(
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        placeholder = { Text(text = stringResource(R.string.password)) },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image =
                                if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisibility) stringResource(R.string.nascondi_password)
                                else stringResource(R.string.mostra_password)

                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        isError = viewModel.passwordError.value != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.passwordError.value != null) {
                        Text(
                            text = stringResource(R.string.campo_vuoto),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.getAutoLogin(),
                        onCheckedChange = { viewModel.setAutoLogin(!viewModel.getAutoLogin()) }
                    )
                    Text(text = stringResource(R.string.resta_collegato))
                }
                Button(onClick = {
                    onLoginAttempted()
                }) {
                    Text(text = stringResource(R.string.login))
                }

            }
        }
        if (loading.value) {
            Dialog(onDismissRequest = {}) {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(all = MaterialTheme.spacing.large)
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.medium))
                        Text(text = stringResource(R.string.accesso_in_corso))
                    }
                }
            }
        }
    }
}