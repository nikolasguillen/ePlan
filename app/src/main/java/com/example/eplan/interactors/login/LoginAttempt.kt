package com.example.eplan.interactors.login

import com.example.eplan.domain.data.DataState
import com.example.eplan.network.LoginService
import com.example.eplan.network.model.CredentialsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class LoginAttempt
@Inject
constructor(
    private val service: LoginService
) {
    fun execute(
        username: String,
        password: String
    ): Flow<DataState<Pair<Int, String>>> = flow {
        try {
            emit(DataState.loading())

            // TODO controlla che ci sia connessione a internet
            val response = getLoginResponse(username = username, password = password)

            emit(DataState.success(response))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getLoginResponse(
        username: String,
        password: String
    ): Pair<Int, String> {
        val response = service.login(CredentialsDto(username = username, password = password))
        return Pair(response.statusCode, response.message)
    }
}