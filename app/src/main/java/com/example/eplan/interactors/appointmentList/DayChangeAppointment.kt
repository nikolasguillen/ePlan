package com.example.eplan.interactors.appointmentList

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Appointment
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.services.AppointmentService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DayChangeAppointment
@Inject
constructor(
    private val service: AppointmentService,
    private val mapper: AppointmentDtoMapper
) {
    fun execute(
        token: String,
        query: String
    ): Flow<DataState<List<Appointment>>> = flow {

        emit(DataState.loading())

        //TODO controlla che ci sia connessione a internet
        val appointments = getAppointmentsFromNetwork(token = token, query = query)
        delay(300)

        emit(DataState.success(appointments))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getAppointmentsFromNetwork(
        token: String,
        query: String
    ): List<Appointment> {
        return mapper.toDomainList(
            service.getDayAppointments(
                token = token,
                start = query,
                end = query
            )
        )
    }
}