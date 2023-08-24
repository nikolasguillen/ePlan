package com.example.eplan.interactors.login

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.network.model.CredentialsDto
import com.example.eplan.network.services.LoginService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginAttempt
@Inject
constructor(
    private val service: LoginService,
    private val preferences: Preferences
) {
    fun execute(
        username: String,
        password: String
    ): Flow<DataState<Pair<Int, String>>> = flow {

        emit(DataState.loading())

        // TODO controlla che ci sia connessione a internet
        val response =
            getLoginResponse(username = username, password = password, preferences = preferences)
        delay(300)

        emit(DataState.success(response))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getLoginResponse(
        username: String,
        password: String,
        preferences: Preferences
    ): Pair<Int, String> {
        val response = service.login(CredentialsDto(username = username, password = password))
        preferences.saveUsername(username = username)
        preferences.saveToken(token = response.message)

        return Pair(response.statusCode, response.message)
    }
}