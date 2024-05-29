package com.cosmobile.eplan.core.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetToken(
    private val preferences: Preferences
) {
    fun execute(): Flow<DataState<String>> = flow {

        emit(DataState.loading())

        val token = preferences.loadToken()
            ?: throw Exception()

        emit(DataState.success(token))
    }.catch { emit(DataState.error(UiText.StringResource(R.string.sessione_scaduta))) }
}