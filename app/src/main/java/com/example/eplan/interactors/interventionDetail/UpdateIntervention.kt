package com.example.eplan.interactors.interventionDetail

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Intervention
import com.example.eplan.network.services.InterventionService
import com.example.eplan.network.model.InterventionDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateIntervention
@Inject
constructor(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        intervention: Intervention
    ): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            //TODO controlla che ci sia connessione internet
            sendIntervention(token = token, intervention = intervention)
            delay(2000)

            emit(DataState.success(true))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun sendIntervention(
        token: String,
        intervention: Intervention
    ) {
        service.updateActivity(
            token = token,
            interventionDto = mapper.mapFromDomainModel(intervention)
        )
    }
}