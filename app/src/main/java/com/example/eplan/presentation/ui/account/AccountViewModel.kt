package com.example.eplan.presentation.ui.account

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.eplan.cache.UserDao
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.util.STAY_LOGGED
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    getToken: GetToken,
    private val getProfilePicUri: GetProfilePicUri,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : EplanViewModel() {

    var imageUri: Uri = Uri.EMPTY
    val accountItems = listOf(
        AccountItems.Settings,
        AccountItems.TimeStats,
        AccountItems.VacationRequest
    )

    init {
        getToken(getToken = getToken)
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