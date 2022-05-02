package com.example.eplan.interactors.appointmentDetail

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Appointment
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.services.AppointmentService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAppointmentById
@Inject
constructor(
    private val service: AppointmentService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        id: String
    ): Flow<DataState<Appointment>> = flow {
        try {
            emit(DataState.loading())

            //TODO controlla che ci sia connessione a internet
            val appointment = getAppointmentFromNetwork(token = token, id = id)
            delay(300)

            emit(DataState.success(appointment))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getAppointmentFromNetwork(
        token: String,
        id: String
    ): Appointment {
        return mapper.mapToDomainModel(
            service.getAppointment(
                token = token,
                id = id
            )
        )
    }
}