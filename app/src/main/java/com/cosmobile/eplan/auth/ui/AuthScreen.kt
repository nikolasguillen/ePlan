package com.cosmobile.eplan.auth.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cosmobile.eplan.R
import com.cosmobile.eplan.auth.ui.AuthEvent.OnLogin
import com.cosmobile.eplan.auth.ui.AuthEvent.OnPasswordChange
import com.cosmobile.eplan.auth.ui.AuthEvent.OnStayLoggedInClick
import com.cosmobile.eplan.auth.ui.AuthEvent.OnUsernameChange
import com.cosmobile.eplan.auth.ui.AuthEvent.TryAutoLogin
import com.cosmobile.eplan.auth.ui.components.AuthAnimation
import com.cosmobile.eplan.core.presentation.theme.AppTheme
import com.cosmobile.eplan.core.util.spacing

@ExperimentalMaterial3Api
@Composable
fun AuthScreen(
    state: LoginUiState,
    onEvent: (AuthEvent) -> Unit,
    onFinish: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    BackHandler(enabled = true) {
        onFinish()
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

        Image(
            painter = painterResource(id = R.drawable.logo_eplan),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary,
                blendMode = BlendMode.SrcAtop
            )
        )

        state.errorMessage?.let {
            Text(text = it.asString(context), color = Color.Red)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            OutlinedTextField(
                value = state.username,
                onValueChange = { onEvent(OnUsernameChange(it)) },
                placeholder = { Text(text = stringResource(R.string.username)) },
                isError = state.usernameError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            onEvent(
                                TryAutoLogin(
                                    activity = context as Activity,
                                    onSuccess = { focusManager.clearFocus(); keyboardController?.hide() }
                                )
                            )
                        }
                    }
            )

            if (state.usernameError != null) {
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
                value = state.password,
                onValueChange = { onEvent(OnPasswordChange(it)) },
                placeholder = { Text(text = stringResource(R.string.password)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val image =
                        if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    val description =
                        if (showPassword) stringResource(R.string.nascondi_password)
                        else stringResource(R.string.mostra_password)

                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = image, description)
                    }
                },
                isError = state.passwordError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            onEvent(
                                TryAutoLogin(
                                    activity = context as Activity,
                                    onSuccess = { focusManager.clearFocus(); keyboardController?.hide() }
                                )
                            )
                        }
                    }
            )

            if (state.passwordError != null) {
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
                onClick = { onEvent(OnStayLoggedInClick) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        ) {
            Checkbox(
                checked = state.stayLogged,
                onCheckedChange = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onEvent(OnStayLoggedInClick)
                }
            )
            Text(
                text = stringResource(R.string.resta_collegato),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))


        Button(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onEvent(OnLogin(context as Activity))
            }
        ) {
            Text(text = stringResource(R.string.login).uppercase())
        }

    }

    AnimatedVisibility(
        visible = state.loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AuthAnimation(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .clickable(
                    enabled = true,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )
    }

    if (!state.showScreen) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .clickable(
                    enabled = true,
                    onClick = {},
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_eplan),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.primary,
                    blendMode = BlendMode.SrcAtop
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "id:pixel_8_pro", showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    AppTheme {
        AuthScreen(
            state = LoginUiState(showScreen = true),
            onEvent = {},
            onFinish = {}
        )
    }
}