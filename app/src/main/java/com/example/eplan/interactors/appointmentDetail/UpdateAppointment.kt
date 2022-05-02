package com.example.eplan.interactors.appointmentDetail

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Appointment
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.services.AppointmentService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class UpdateAppointment
@Inject
constructor(
    private val service: AppointmentService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        appointment: Appointment
    ): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            // TODO controlla che ci sia connessione a internet
            sendAppointment(token = token, appointment = appointment)
            delay(1000)

            emit(DataState.success(true))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun sendAppointment(
        token: String,
        appointment: Appointment
    ) {
        service.updateAppointment(
            token = token,
            appointmentDto = mapper.mapFromDomainModel(appointment)
        )
    }
}