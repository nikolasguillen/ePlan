package com.example.eplan.interactors.interventionDetail

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Intervention
import com.example.eplan.network.model.InterventionDtoMapper
import com.example.eplan.network.services.InterventionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetInterventionById
@Inject
constructor(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        id: String
    ): Flow<DataState<Intervention>> = flow {

        emit(DataState.loading())

        val intervention = getInterventionFromNetwork(token = token, id = id)

        emit(DataState.success(intervention))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getInterventionFromNetwork(
        token: String,
        id: String
    ): Intervention {
        return mapper.mapToDomainModel(
            service.getIntervention(
                token = token,
                id = id
            )
        )
    }
}