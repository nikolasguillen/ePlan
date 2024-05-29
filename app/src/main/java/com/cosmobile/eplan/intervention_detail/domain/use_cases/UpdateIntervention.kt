package com.cosmobile.eplan.intervention_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UpdateIntervention(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        intervention: Intervention
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        sendIntervention(token = token, intervention = intervention)
        delay(500)

        emit(DataState.success(true))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun sendIntervention(
        token: String,
        intervention: Intervention
    ) {
        val dto = mapper.mapFromDomainModel(intervention)

        service.updateIntervention(
            token = token,
            interventionDto = dto
        )
    }
}