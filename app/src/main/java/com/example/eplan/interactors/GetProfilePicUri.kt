package com.example.eplan.interactors

import android.net.Uri
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetProfilePicUri(
    private val preferences: Preferences
) {
    fun execute(): Flow<DataState<Uri>> = flow {

        emit(DataState.loading())

        val uri = preferences.loadProfilePicUri() ?: throw Exception()

        emit(DataState.success(Uri.parse(uri)))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }
}