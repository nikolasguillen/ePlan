package com.example.eplan.interactors.camera

import android.net.Uri
import com.example.eplan.cache.UserDao
import com.example.eplan.domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

        emit(DataState.loading())

        val response = getSaveUriResponse(uri = imageUri)

        emit(DataState.success(response))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getSaveUriResponse(uri: Uri) {
        val username = userDao.getUsername()
        if (username != null) {
            userDao.saveImageUri(username = username, uri = uri.toString())
        }
    }
}