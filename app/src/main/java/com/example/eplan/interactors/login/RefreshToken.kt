package com.example.eplan.interactors.login

import android.content.SharedPreferences
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.network.services.RefreshTokenService
import com.example.eplan.presentation.util.STAY_LOGGED
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
    fun execute(): Flow<DataState<Pair<Int, String>>> = flow {
        emit(DataState.loading())

        val stayLogged = sharedPreferences.getBoolean(STAY_LOGGED, false)
        val token = encryptedPreferences.loadToken()

        // TODO controlla che ci sia connessione a internet
        if (stayLogged && token != null) {
            val response = getNewTokenResponse(token = token)
            emit(DataState.success(response))
        } else {
            emit(DataState.error("Token non più valido, effettua nuovamente il login"))
        }
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getNewTokenResponse(
        token: String
    ): Pair<Int, String> {
        val response = service.getNewToken(token = token)
        encryptedPreferences.saveToken(token = response.message)

        return Pair(response.statusCode, response.message)
    }
}