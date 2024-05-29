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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetDescriptionSuggestions(
    private val service: InterventionService,
    private val mapper: InterventionDtoMapper
) {
    operator fun invoke(
        token: String,
        date: LocalDate,
        activityId: String
    ): Flow<DataState<List<String>>> = flow {
        emit(DataState.loading())

        val relatedInterventions = getRelatedInterventionsFromNetwork(
            token = token,
            date = date,
            activityId = activityId
        )

        val descriptionOccurrences = relatedInterventions
            .groupBy { it.description.lowercase() }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .take(5)
            .map { suggestion -> suggestion.first.replaceFirstChar { it.uppercase() } }

        emit(DataState.success(descriptionOccurrences))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getRelatedInterventionsFromNetwork(
        token: String,
        date: LocalDate,
        activityId: String
    ): List<Intervention> {
        return mapper.toDomainList(
            service.getPeriodInterventions(
                token = token,
                start = date.minusMonths(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                end = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            )
        )
            .filter { it.activityId == activityId && it.description.isNotBlank() }
    }
}