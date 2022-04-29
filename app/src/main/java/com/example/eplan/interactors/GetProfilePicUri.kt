package com.example.eplan.interactors

import android.net.Uri
import android.util.Log
import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetProfilePicUri(
    private val userDao: UserDao
) {
    fun execute(): Flow<DataState<Uri>> = flow {
        try {
            emit(DataState.loading())

            val uri = getUriFromCache()

            emit(DataState.success(Uri.parse(uri)))
        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error(e.message?: "Errore sconosciuto"))
        }
    }

    private suspend fun getUriFromCache(): String {
        return userDao.getImageUri()
    }
}