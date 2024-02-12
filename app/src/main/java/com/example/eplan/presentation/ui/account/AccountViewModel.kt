package com.example.eplan.presentation.ui.account

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.eplan.R
import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.ui.account.AccountEvent.GetUriEvent
import com.example.eplan.presentation.ui.account.AccountEvent.Logout
import com.example.eplan.presentation.util.Dialog
import com.example.eplan.presentation.util.STAY_LOGGED
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.UiText
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
    private val encryptedPreferences: Preferences,
    private val sharedPreferences: SharedPreferences
) : EplanViewModel() {

    var imageUri: Uri = Uri.EMPTY
    val username = encryptedPreferences.loadUsername()
    val accountItems = listOf(
        AccountItems.Settings,
        AccountItems.TimeStats,
        AccountItems.VacationRequest
    )

    var state by mutableStateOf(AccountUiState())
        private set

    init {
        getToken(getToken = getToken)
    }

    fun onTriggerEvent(event: AccountEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetUriEvent -> {
                        getImageUri()
                    }

                    is Logout -> {
                        logout(event.onLogout)
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
                state = state.copy(imageUri = uri)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getImageUri: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun logout(onLogout: () -> Unit) {
        val dialog = Dialog(
            title = UiText.StringResource(R.string.attenzione),
            message = UiText.StringResource(R.string.logout_message),
            dismissText = UiText.StringResource(R.string.annulla),
            confirmText = UiText.StringResource(R.string.ok_conf),
            onDismiss = {
                state = state.copy(dialog = null)
            },
            onConfirm = {
                state = state.copy(dialog = null)
                sharedPreferences.edit().remove(STAY_LOGGED).apply()
                encryptedPreferences.deleteUsername()
                encryptedPreferences.deleteToken()
                onLogout()
            }
        )
        state = state.copy(dialog = dialog)
    }
}