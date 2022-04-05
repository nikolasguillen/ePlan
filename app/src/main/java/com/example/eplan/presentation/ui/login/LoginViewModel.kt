package com.example.eplan.presentation.ui.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.presentation.ui.login.LoginEvent.*
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val loginAttempt: LoginAttempt
) : ViewModel() {

    val username = mutableStateOf("n.guillen")
    val password = mutableStateOf("0FeZbLUO")
    val statusCode = mutableStateOf(0)
    val message = mutableStateOf("")
    val loading = mutableStateOf(false)
    val successfulLoginAttempt = mutableStateOf(false)

    private var userToken = ""

    fun onTriggerEvent(event: LoginEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is LoginAttemptEvent -> {
                        login(username.value, password.value)
                    }
                }
            } catch (e: Exception) {
                Log.e("ActivityListViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }

    fun getToken(): String {
        return userToken
    }

    private fun login(username: String, password: String) {

        loginAttempt.execute(username = username, password = password).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { pair ->
                statusCode.value = pair.first
                message.value = pair.second
                successfulLoginAttempt.value = true
                userToken = message.value

            }

            dataState.error?.let { error ->
                Log.e(TAG, "loginAttempt: $error")
                message.value = error
                successfulLoginAttempt.value = false
                // TODO Gestire errori
            }
        }.launchIn(viewModelScope)

        Log.d(TAG, "status and message: $statusCode, $message")
    }
}