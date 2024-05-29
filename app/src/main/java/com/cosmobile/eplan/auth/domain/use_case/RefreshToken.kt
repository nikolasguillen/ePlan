package com.cosmobile.eplan.auth.domain.use_case

import android.content.SharedPreferences
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.data.model.ServerResponse
import com.cosmobile.eplan.core.data.services.RefreshTokenService
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.util.STAY_LOGGED
import com.cosmobile.eplan.core.util.USER_TOKEN
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshToken
@Inject
constructor(
    private val service: RefreshTokenService,
    private val encryptedPreferences: Preferences,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Flow<DataState<ServerResponse>> = flow {
        emit(DataState.loading())

        val stayLogged = sharedPreferences.getBoolean(STAY_LOGGED, false)
        val token = encryptedPreferences.loadToken()

        if (stayLogged && token != null) {
            val response = getNewTokenResponse(token = USER_TOKEN + token)
            if (response.isOk()) {
                encryptedPreferences.saveToken(token = response.message)
                return@flow emit(DataState.success(response))
            } else {
                sharedPreferences.edit().putBoolean(STAY_LOGGED, false).apply()
                encryptedPreferences.deleteToken()
                return@flow emit(DataState.error(UiText.StringResource(R.string.token_scaduto)))
            }

        } else {
            emit(DataState.error(UiText.StringResource(R.string.token_scaduto)))
        }
    }.catch { throwable ->
        emit(
            DataState.error(
                throwable.message?.let {
                    UiText.DynamicString(it)
                } ?: UiText.StringResource(R.string.errore_sconosciuto)
            )
        )
    }

    private suspend fun getNewTokenResponse(
        token: String
    ): ServerResponse {
        val response = service.getNewToken(token = token)
        return ServerResponse(code = response.statusCode, message = response.message)
    }
}