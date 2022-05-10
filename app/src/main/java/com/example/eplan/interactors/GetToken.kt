package com.example.eplan.interactors

import android.util.Log
import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetToken(
    private val userDao: UserDao
) {
    fun execute(): Flow<DataState<String>> = flow {

        emit(DataState.loading())

        val token = getTokenFromCache()

        emit(DataState.success(token))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getTokenFromCache(): String {
        return userDao.getUserToken()
    }
}