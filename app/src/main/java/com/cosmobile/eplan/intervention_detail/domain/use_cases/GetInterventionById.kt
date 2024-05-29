package com.cosmobile.eplan.intervention_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetInterventionById(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        id: String
    ): Flow<DataState<Intervention>> = flow {

        emit(DataState.loading())

        val intervention = getInterventionFromNetwork(token = token, id = id)
        if (intervention == null) {
            emit(DataState.error(UiText.StringResource(R.string.errore_sconosciuto)))
            return@flow
        }

        emit(DataState.success(intervention))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getInterventionFromNetwork(
        token: String,
        id: String
    ): Intervention? {
        return mapper.mapToDomainModel(
            service.getIntervention(
                token = token,
                id = id
            )
        )
    }
}