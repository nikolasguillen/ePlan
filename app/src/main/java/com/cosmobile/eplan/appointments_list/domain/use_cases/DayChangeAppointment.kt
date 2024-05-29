package com.cosmobile.eplan.appointments_list.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointments_list.data.services.AppointmentsListService
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class DayChangeAppointment
@Inject
constructor(
    private val appointmentsListService: AppointmentsListService,
    private val activitiesService: ActivityService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<DataState<List<Appointment>>> = flow {

        emit(DataState.loading())

        val activities = activitiesService.getUserActivities(token = token)
        val appointments = getAppointmentsFromNetwork(
            token = token,
            startDate = startDate,
            endDate = endDate
        ).map {
            if (it.title.isBlank()) {
                it.copy(
                    title = activities
                        .find { activity -> activity.activityId == it.activityId }?.activityName
                        ?: it.title
                )
            } else {
                it
            }
        }



        emit(DataState.success(appointments))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getAppointmentsFromNetwork(
        token: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Appointment> {
        return mapper.toDomainList(
            appointmentsListService.getDayAppointments(
                token = token,
                start = startDate.toString(),
                end = endDate.toString()
            ).filter { it.activityId != null }
        )
    }
}