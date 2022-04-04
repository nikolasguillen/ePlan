package com.example.eplan.interactors.workActivityList

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DayChange(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) {
    fun execute(
        token: String,
        query: String
    ): Flow<DataState<List<WorkActivity>>> = flow {
        try {
            emit(DataState.loading())

            /*TODO da togliere, solo per vedere animazione di caricamento*/
            delay(1000)

            // TODO controlla che ci sia connessione a internet
            val recipes = getWorkActivitiesFromNetwork(token = token, query = query)

            emit(DataState.success(recipes))

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
            ).workActivities
        )
    }
}