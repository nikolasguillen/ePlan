package com.example.eplan.interactors.workActivityList

import android.util.Log
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DayChange
@Inject
constructor(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) {
    fun execute(
        token: String,
        query: String
    ): Flow<DataState<List<WorkActivity>>> = flow {
        try {
            emit(DataState.loading())

            // TODO controlla che ci sia connessione a internet
            val workActivities = getWorkActivitiesFromNetwork(token = token, query = query)

            emit(DataState.success(workActivities))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getWorkActivitiesFromNetwork(
        token: String,
        query: String
    ): List<WorkActivity> {
        return mapper.toDomainList(
            service.getDayActivities(
                token = token,
                query = query
            )
        )
    }
}