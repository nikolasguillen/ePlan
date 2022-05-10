package com.example.eplan.interactors

import android.net.Uri
import android.util.Log
import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetProfilePicUri(
    private val userDao: UserDao
) {
    fun execute(): Flow<DataState<Uri>> = flow {

        emit(DataState.loading())

        val uri = getUriFromCache()

        emit(DataState.success(Uri.parse(uri)))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getUriFromCache(): String {
        return userDao.getImageUri()
    }
}