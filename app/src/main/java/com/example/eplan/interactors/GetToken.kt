package com.example.eplan.interactors

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetToken(
    private val preferences: Preferences
) {
    fun execute(): Flow<DataState<String>> = flow {

        emit(DataState.loading())

        val token = preferences.loadToken() ?: throw Exception()

        emit(DataState.success(token))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }
}