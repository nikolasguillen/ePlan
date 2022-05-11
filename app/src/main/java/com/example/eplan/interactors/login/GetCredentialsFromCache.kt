package com.example.eplan.interactors.login

import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCredentialsFromCache
@Inject
constructor(
    private val userDao: UserDao
) {
    fun execute(): Flow<DataState<Pair<String, String>>> = flow {
        emit(DataState.loading())

        val response = getCredentials(userDao = userDao)

        if (response.first == null || response.second == null) {
            throw Exception("Credenziali non trovate sul dispositivo, si prega di reinserirle")
        } else {
            response.first?.let { first ->
                response.second?.let { second ->
                    emit(DataState(Pair(first, second)))
                }
            }
        }
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getCredentials(
        userDao: UserDao
    ): Pair<String?, String?> {
        return Pair(first = userDao.getUsername(), second = userDao.getPassword())
    }
}