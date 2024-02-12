package com.example.eplan.interactors.vacationRequest

import com.example.eplan.domain.data.DataState
import com.example.eplan.network.model.VacationRequestDto
import com.example.eplan.network.services.VacationRequestService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class RequestVacation
@Inject
constructor(
    private val service: VacationRequestService
) {
    fun execute(
        token: String,
        startDateMillis: Long?,
        endDateMillis: Long?
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        if (startDateMillis == null) {
            throw Exception("Data di inizio non valida")
        }

        val overriddenEndDateMillis = endDateMillis ?: startDateMillis
        if (overriddenEndDateMillis < startDateMillis) {
            throw Exception("Data di fine non valida")
        }

        // Converti i millisecondi a un oggetto Instant
        val instantStart = Instant.ofEpochMilli(startDateMillis)
        val instantEnd = Instant.ofEpochMilli(overriddenEndDateMillis)

        // Specifica una zona oraria se necessario
        val zonaOraria = ZoneId.systemDefault()

        // Converti l'Instant a LocalDate
        val startDate = instantStart.atZone(zonaOraria).toLocalDate()
        val endDate = instantEnd.atZone(zonaOraria).toLocalDate()

        val vacationRequestDto = VacationRequestDto(
            startDate = startDate.toString(),
            endDate = endDate.toString()
        )

        sendVacationRequest(token = token, vacationRequestDto = vacationRequestDto)
        delay(300)

        emit(DataState.success(true))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun sendVacationRequest(
        token: String,
        vacationRequestDto: VacationRequestDto
    ) {
        service.vacationRequest(token = token, vacationRequestDto = vacationRequestDto)
    }
}