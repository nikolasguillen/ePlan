package com.example.eplan.presentation.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.login.GetCredentialsFromCache
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.presentation.ui.login.LoginEvent.LoginAttemptEvent
import com.example.eplan.presentation.util.STAY_LOGGED
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val loginAttempt: LoginAttempt,
    private val getCredentialsFromCache: GetCredentialsFromCache,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val username = mutableStateOf("n.guillen")
    val password = mutableStateOf("0FeZbLUO")
    var usernameError: MutableState<String?> = mutableStateOf(null)
    var passwordError: MutableState<String?> = mutableStateOf(null)
    private val statusCode = mutableStateOf(0)
    val message = mutableStateOf("")
    val loading = mutableStateOf(false)
    private val _successfulLoginAttempt = MutableStateFlow(false)
    val successfulLoginAttempt = _successfulLoginAttempt.asStateFlow()
    private var stayLogged by mutableStateOf(sharedPreferences.getBoolean(STAY_LOGGED, false))
    private var userToken = ""

    fun setAutoLogin(value: Boolean) {
        stayLogged = value
        sharedPreferences.edit().putBoolean(STAY_LOGGED, stayLogged).apply()
    }

    fun getAutoLogin(): Boolean {
        return stayLogged
    }

    init {
        if (stayLogged) {
            getCredentialsFromCache()
        }
    }

    fun onTriggerEvent(event: LoginEvent) {
        when (event) {
            is LoginAttemptEvent -> {
                login(username.value, password.value)
            }
        }
    }

    fun getToken(): String {
        return userToken
    }

    private fun login(username: String, password: String) {

        message.value = ""

        if (username.isNotBlank() && password.isNotBlank()) {
            usernameError.value = null
            passwordError.value = null

            loginAttempt.execute(username = username, password = password).onEach { dataState ->

                loading.value = dataState.loading

                dataState.data?.let { pair ->
                    statusCode.value = pair.first
                    userToken = pair.second
                    _successfulLoginAttempt.value = true
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "loginAttempt: $error")
                    message.value = error
                    _successfulLoginAttempt.value = false
                    // TODO Gestire errori
                }
            }.launchIn(viewModelScope)
        } else {
            if (username.isBlank()) usernameError.value = "Questo campo non può essere vuoto" else usernameError.value = null
            if (password.isBlank()) passwordError.value = "Questo campo non può essere vuoto" else passwordError.value = null
        }
    }

    private fun getCredentialsFromCache() {
        getCredentialsFromCache.execute().onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { credentials ->
                onTriggerEvent(LoginAttemptEvent)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getCredentialsFromCache: $error")
                message.value = error
            }
        }.launchIn(viewModelScope)
    }
}