package com.example.eplan.interactors.login

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCredentialsFromCache
@Inject
constructor(
    private val preferences: Preferences
) {
    fun execute(): Flow<DataState<String>> = flow {
        emit(DataState.loading())

        val response = getToken(preferences = preferences)

        if (response == null) {
            throw Exception("Credenziali non trovate sul dispositivo, si prega di reinserirle")
        } else {
            emit(DataState(response))
        }
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private fun getToken(
        preferences: Preferences
    ): String? {
        return preferences.loadToken()
    }
}