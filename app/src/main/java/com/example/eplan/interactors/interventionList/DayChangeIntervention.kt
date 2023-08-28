package com.example.eplan.interactors.interventionList

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Intervention
import com.example.eplan.network.services.InterventionService
import com.example.eplan.network.model.InterventionDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

        emit(DataState.loading())

        val interventions = getInterventionsFromNetwork(token = token, query = query)
        delay(300)

        emit(DataState.success(interventions))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getInterventionsFromNetwork(
        token: String,
        query: String
    ): List<Intervention> {
        return mapper.toDomainList(
            service.getDayInterventions(
                token = token,
                start = query,
                end = query
            )
        )
    }
}