package com.cosmobile.eplan.account

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.R
import com.cosmobile.eplan.account.AccountEvent.Logout
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.domain.use_cases.ClearUserData
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.presentation.EplanViewModel
import com.cosmobile.eplan.core.util.Dialog
import com.cosmobile.eplan.core.util.USER_NAME
import com.cosmobile.eplan.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    getToken: GetToken,
    private val clearUserData: ClearUserData,
    encryptedPreferences: Preferences,
    sharedPreferences: SharedPreferences
) : EplanViewModel() {

    val username = sharedPreferences.getString(USER_NAME, "") ?: ""
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
                    is Logout -> {
                        logout(event.onLogout)
                    }
                }
            } catch (e: Exception) {
                Log.e("AccountViewModel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
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
                clearUserData(onLogout)
            }
        )
        state = state.copy(dialog = dialog)
    }
}