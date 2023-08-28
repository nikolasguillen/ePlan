package com.example.eplan.interactors.vacationRequest

import com.example.eplan.domain.data.DataState
import com.example.eplan.network.model.VacationRequestDto
import com.example.eplan.network.services.VacationRequestService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class RequestVacation
@Inject
constructor(
    private val service: VacationRequestService
) {
    fun execute(
        token: String,
        startDate: LocalDate,
        endDate: LocalDate? = null
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        val vacationRequestDto = VacationRequestDto(
            startDate = startDate.toString(),
            endDate = endDate?.toString()
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