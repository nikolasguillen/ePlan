package com.example.eplan.presentation.ui.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val repository: LoginRepository
) : ViewModel() {

    val username = mutableStateOf("")
    val password = mutableStateOf("")
    private var userToken = ""

    fun onTriggerEvent(event: LoginEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is LoginEvent.LoginAttemptEvent -> {
                        login(event.username, event.password)
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

    private suspend fun login(username: String, password: String) {
        userToken = repository.login(username = username, password = password)
    }
}