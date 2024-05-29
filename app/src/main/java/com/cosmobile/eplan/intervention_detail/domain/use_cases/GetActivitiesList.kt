package com.cosmobile.eplan.intervention_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.data.model.ActivityDtoMapper
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.core.domain.model.Activity
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class GetActivitiesList(
    private val activityService: ActivityService,
    private val interventionService: InterventionService,
    private val activityMapper: ActivityDtoMapper,
    private val interventionMapper: InterventionDtoMapper
) {
    fun execute(token: String, date: LocalDate): Flow<DataState<List<Activity>>> = flow {

        emit(DataState.loading())

        val activities = getActivitiesFromNetwork(token = token)
        val lastMonthInterventions = getLastMonthInterventions(token = token, date = date)

        // get the most repeated activityIDs from the last month interventions
        val mostRepeatedActivityIDs = lastMonthInterventions
            .groupBy { it.activityId }
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }
            .map { it.first }

        //sort activities by values in mostRepeatedActivityIDs
        val sortedRepeatedActivities = activities
            .sortedBy { activity ->
                mostRepeatedActivityIDs.indexOf(activity.id).takeIf { it != -1 } ?: Int.MAX_VALUE
            }

        emit(DataState.success(sortedRepeatedActivities))

    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getActivitiesFromNetwork(
        token: String
    ): List<Activity> {
        return activityMapper.toDomainList(activityService.getUserActivities(token = token))
    }

    private suspend fun getLastMonthInterventions(
        token: String,
        date: LocalDate
    ): List<Intervention> {
        return interventionMapper.toDomainList(
            interventionService.getPeriodInterventions(
                token = token,
                start = date.minusMonths(1).toString(),
                end = date.toString()
            )
        )
    }
}