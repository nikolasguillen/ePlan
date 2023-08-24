package com.example.eplan.interactors.camera

import android.net.Uri
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveProfilePicUri
@Inject
constructor(
    private val preferences: Preferences
) {
    fun execute(
        imageUri: Uri
    ): Flow<DataState<Unit>> = flow {

        emit(DataState.loading())

        val response = getSaveUriResponse(uri = imageUri, preferences = preferences)

        emit(DataState.success(response))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private fun getSaveUriResponse(uri: Uri, preferences: Preferences) {
        val username = preferences.loadUsername()
        if (username != null) {
            preferences.saveProfilePicUri(uri = uri.toString())
        }
    }
}