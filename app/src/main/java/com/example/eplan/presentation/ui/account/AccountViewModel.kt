package com.example.eplan.presentation.ui.account

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.cache.UserDao
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.presentation.util.STAY_LOGGED
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    private val getToken: GetToken,
    private val getProfilePicUri: GetProfilePicUri,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var userToken = USER_TOKEN
    var imageUri: Uri = Uri.EMPTY

    init {
        getToken()
    }

    fun onTriggerEvent(event: AccountEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is AccountEvent.GetUriEvent -> {
                        getImageUri()
                    }
                    is AccountEvent.Logout -> {
                        logout()
                    }
                }
            } catch (e: Exception) {
                Log.e("AccountViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->

            dataState.data?.let { token ->
                userToken += token
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }

    private fun getImageUri() {
        getProfilePicUri.execute().onEach { dataState ->

            dataState.data?.let { uri ->
                imageUri = uri
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getImageUri: $error")
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun logout() {
        sharedPreferences.edit().putBoolean(STAY_LOGGED, false).apply()
        userDao.deleteAllUsers()
    }
}