package com.cosmobile.eplan.appointment_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.data.services.AppointmentDetailService
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UpdateAppointment(
    private val service: AppointmentDetailService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        appointment: Appointment
    ): Flow<DataState<Boolean>> = flow {

        emit(DataState.loading())

        sendAppointment(token = token, appointment = appointment)
        delay(500)

        emit(DataState.success(true))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun sendAppointment(
        token: String,
        appointment: Appointment
    ) {
        service.updateAppointment(
            token = token,
            appointmentListItemDto = mapper.mapFromDomainModel(appointment)
        )
    }
}