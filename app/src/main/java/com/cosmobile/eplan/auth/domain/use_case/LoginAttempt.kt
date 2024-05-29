package com.cosmobile.eplan.auth.domain.use_case

import com.cosmobile.eplan.R
import com.cosmobile.eplan.auth.data.model.CredentialsDto
import com.cosmobile.eplan.auth.data.model.LoginResponse
import com.cosmobile.eplan.auth.data.services.LoginService
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LoginAttempt(
    private val service: LoginService,
    private val encryptedPreferences: Preferences
) {
    fun execute(
        username: String,
        password: String
    ): Flow<DataState<LoginResponse>> = flow {

        emit(DataState.loading())

        val response = getLoginResponse(
            username = username,
            password = password
        )

        if (response.isOk()) {
            delay(2000)
            encryptedPreferences.saveToken(response.message)
            return@flow emit(DataState.success(response))
        } else {
            return@flow emit(DataState.error(UiText.DynamicString(response.message)))
        }
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getLoginResponse(
        username: String,
        password: String
    ): LoginResponse {
        return service.login(CredentialsDto(username = username, password = password))
    }
}