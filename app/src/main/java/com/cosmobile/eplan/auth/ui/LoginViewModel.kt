package com.cosmobile.eplan.auth.ui

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.R
import com.cosmobile.eplan.auth.domain.use_case.LoginAttempt
import com.cosmobile.eplan.auth.domain.use_case.RefreshToken
import com.cosmobile.eplan.auth.domain.use_case.SaveUserData
import com.cosmobile.eplan.auth.ui.AuthEvent.OnLogin
import com.cosmobile.eplan.auth.ui.AuthEvent.OnPasswordChange
import com.cosmobile.eplan.auth.ui.AuthEvent.OnStayLoggedInClick
import com.cosmobile.eplan.auth.ui.AuthEvent.OnUsernameChange
import com.cosmobile.eplan.auth.ui.AuthEvent.TryAutoLogin
import com.cosmobile.eplan.core.domain.use_cases.ClearUserData
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.STAY_LOGGED
import com.cosmobile.eplan.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val application: Application,
    private val loginAttempt: LoginAttempt,
    private val refreshToken: RefreshToken,
    private val sharedPreferences: SharedPreferences,
    private val saveUserData: SaveUserData,
    clearUserData: ClearUserData
) : ViewModel() {

    var state by mutableStateOf(LoginUiState())
        private set

    private val _successfulLoginChannel = Channel<Unit>()
    val successfulLoginChannel = _successfulLoginChannel.receiveAsFlow()

    private val credentialManager by lazy {
        CredentialManager.create(application)
    }

    init {
        if (sharedPreferences.getBoolean(STAY_LOGGED, false)) {
            state = state.copy(stayLogged = true)
            viewModelScope.launch(Dispatchers.IO) {
                refreshToken.execute().onEach { dataState ->

                    dataState.data?.let {
                        sharedPreferences.edit().putBoolean(STAY_LOGGED, true).apply()
                        _successfulLoginChannel.send(Unit)
                    }

                    dataState.error?.let {
                        Log.e(GENERIC_DEBUG_TAG, "refreshToken: $it")
                        state = state.copy(
                            stayLogged = false,
                            showScreen = true,
                            errorMessage = it
                        )
                    }
                }.launchIn(viewModelScope)
            }
        } else {
            clearUserData {
                state = state.copy(showScreen = true)
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is OnUsernameChange -> {
                state = state.copy(username = event.value)
            }

            is OnPasswordChange -> {
                state = state.copy(password = event.value)
            }

            is OnLogin -> {
                login(event.activity)
            }

            is OnStayLoggedInClick -> {
                state = state.copy(stayLogged = state.stayLogged.not())
            }

            is TryAutoLogin -> {
                if (state.showScreen.not()) return

                viewModelScope.launch {
                    val passwordCredential = getPasswordCredential(
                        activity = event.activity,
                        onSuccess = event.onSuccess
                    ) ?: return@launch
                    withContext(Dispatchers.Main) {
                        state = state.copy(
                            username = passwordCredential.id,
                            password = passwordCredential.password
                        )
                    }
                    login(event.activity)
                }
            }
        }
    }

    private fun login(activity: Activity) {
        state = state.copy(errorMessage = null)

        if (state.username.isBlank()) {
            state = state.copy(usernameError = UiText.StringResource(R.string.campo_obbligatorio))
            return
        }

        if (state.password.isBlank()) {
            state = state.copy(passwordError = UiText.StringResource(R.string.campo_obbligatorio))
            return
        }

        loginAttempt.execute(
            username = state.username,
            password = state.password
        ).onEach { dataState ->
            state = state.copy(loading = true)

            dataState.data?.let { loginResponse ->
                sharedPreferences.edit().putBoolean(STAY_LOGGED, state.stayLogged).apply()
                val userName = "${loginResponse.data?.name} ${loginResponse.data?.surname}"
                val workingHours = loginResponse.data?.checkmailHours
                saveUserData.execute(name = userName, workingHours = workingHours)
                saveCredentials(activity, state.username, state.password)
                _successfulLoginChannel.send(Unit)
            }

            dataState.error?.let { error ->
                Log.e(GENERIC_DEBUG_TAG, "loginAttempt: $error")
                state = state.copy(errorMessage = error, loading = false)
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun saveCredentials(activity: Activity, username: String, password: String) {
        try {
            credentialManager.createCredential(
                request = CreatePasswordRequest(username, password),
                context = activity
            )
            Log.v(GENERIC_DEBUG_TAG, "Credentials successfully added")
        } catch (e: CreateCredentialCancellationException) {
            //do nothing, the user chose not to save the credential
            Log.v("CredentialTest", "User cancelled the save")
        } catch (e: CreateCredentialException) {
            Log.v("CredentialTest", "Credential save error", e)
        }
    }

    private suspend fun getPasswordCredential(
        activity: Activity,
        onSuccess: () -> Unit
    ): PasswordCredential? {
        try {
            val getCredRequest = GetCredentialRequest(listOf(GetPasswordOption()))
            val credentialResponse = credentialManager.getCredential(
                request = getCredRequest,
                context = activity
            )

            onSuccess()
            return credentialResponse.credential as? PasswordCredential
        } catch (e: GetCredentialCancellationException) {
            //User cancelled the request. Return nothing
            return null
        } catch (e: NoCredentialException) {
            //We don't have a matching credential
            return null
        } catch (e: GetCredentialException) {
            Log.e("CredentialTest", "Error getting credential", e)
            return null
        } catch (e: Exception) {
            Log.e("CredentialTest", "Error getting credential", e)
            return null
        }
    }
}