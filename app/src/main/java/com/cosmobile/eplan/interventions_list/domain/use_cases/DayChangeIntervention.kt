package com.cosmobile.eplan.interventions_list.domain.use_cases

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
import java.time.LocalDate

class DayChangeIntervention(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    fun execute(
        token: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<DataState<List<Intervention>>> = flow {

        emit(DataState.loading())

        val interventions =
            getInterventionsFromNetwork(token = token, startDate = startDate, endDate = endDate)
        delay(200)

        emit(DataState.success(interventions))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getInterventionsFromNetwork(
        token: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Intervention> {
        return mapper.toDomainList(
            service.getPeriodInterventions(
                token = token,
                start = startDate.toString(),
                end = endDate.toString()
            ).filter { it.idAttivita != null }
        )
    }
}