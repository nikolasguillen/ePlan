package com.cosmobile.eplan.appointment_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.data.services.AppointmentDetailService
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetAppointmentById
@Inject
constructor(
    private val appointmentDetailService: AppointmentDetailService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        id: String,
        date: LocalDate
    ): Flow<DataState<Appointment>> = flow {

        emit(DataState.loading())

        val appointment =
            getAppointmentFromNetwork(
                token = token,
                id = id,
                date = date
            )

        if (appointment == null) {
            emit(DataState.error(UiText.StringResource(R.string.errore_sconosciuto)))
            return@flow
        }

        emit(DataState.success(appointment))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getAppointmentFromNetwork(
        token: String,
        id: String,
        date: LocalDate
    ): Appointment? {
        return appointmentDetailService.getAppointments(
            token = token,
            start = date.toString(),
            end = date.toString()
        )
            .firstOrNull { it.appointmentId == id }
            ?.let {
                mapper.mapToDomainModel(it)
            }
    }
}