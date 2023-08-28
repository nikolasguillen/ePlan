package com.example.eplan.presentation.ui.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing


@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginAttempted: () -> Unit,
    onSuccessfulLogin: () -> Unit
) {

    val successfulLogin by viewModel.successfulLoginAttempt.collectAsState()
    val loading = viewModel.loading
    val activity = LocalContext.current as Activity
    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.successfulLoginAttempt.collect {
            if (it) onSuccessfulLogin()
        }
    }

    BackHandler(enabled = true) {
        activity.finish()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .clickable(
                enabled = true,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if (!successfulLogin) {
            Text(text = viewModel.message.value, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            OutlinedTextField(
                value = viewModel.username.value,
                onValueChange = { viewModel.username.value = it },
                placeholder = { Text(text = stringResource(R.string.username)) },
                isError = viewModel.usernameError.value != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
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

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.password.value = it },
                placeholder = { Text(text = stringResource(R.string.password)) },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
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

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                enabled = true,
                onClick = {
                    viewModel.setAutoLogin(!viewModel.getAutoLogin())
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        ) {
            Checkbox(
                checked = viewModel.getAutoLogin(),
                onCheckedChange = { viewModel.setAutoLogin(!viewModel.getAutoLogin()) }
            )
            Text(
                text = stringResource(R.string.resta_collegato),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Button(
            onClick = {
                onLoginAttempted()
            }
        ) {
            Text(text = stringResource(R.string.login))
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