package com.example.eplan.interactors.login

import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCredentialsFromCache
@Inject
constructor(
    private val userDao: UserDao
) {
    fun execute(): Flow<DataState<Pair<String, String>>> = flow {
        try {
            emit(DataState.loading())

            val response = getCredentials(userDao = userDao)

            emit(DataState.success(response))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getCredentials(
        userDao: UserDao
    ): Pair<String, String> {
        return Pair(first = userDao.getUsername(), second = userDao.getPassword())
    }
}