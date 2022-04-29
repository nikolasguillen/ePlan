package com.example.eplan.interactors.camera

import android.net.Uri
import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class SaveProfilePicUri
@Inject
constructor(
    private val userDao: UserDao
) {
    fun execute(
        imageUri: Uri
    ): Flow<DataState<Unit>> = flow {
        try {
            emit(DataState.loading())

            val response = getSaveUriResponse(uri = imageUri)

            emit(DataState.success(response))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getSaveUriResponse(uri: Uri) {
        val username = userDao.getUsername()
        userDao.saveImageUri(username = username, uri = uri.toString())
    }
}