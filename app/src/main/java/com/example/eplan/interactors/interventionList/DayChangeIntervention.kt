package com.example.eplan.interactors.interventionList

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Intervention
import com.example.eplan.network.services.InterventionService
import com.example.eplan.network.model.InterventionDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DayChangeIntervention
@Inject
constructor(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        query: String
    ): Flow<DataState<List<Intervention>>> = flow {
        try {
            emit(DataState.loading())

            // TODO controlla che ci sia connessione a internet
            val interventions = getInterventionsFromNetwork(token = token, query = query)
            delay(300)

            emit(DataState.success(interventions))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getInterventionsFromNetwork(
        token: String,
        query: String
    ): List<Intervention> {
        return mapper.toDomainList(
            service.getDayActivities(
                token = token,
                start = query,
                end = query
            )
        )
    }
}