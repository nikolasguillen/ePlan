package com.example.eplan.interactors.timeStats

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.TimeStats
import com.example.eplan.network.model.TimeStatsDtoMapper
import com.example.eplan.network.services.TimeStatsService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTimeStats
@Inject
constructor(
    private val service: TimeStatsService,
    private val mapper: TimeStatsDtoMapper
) {
    fun execute(
        token: String,
        month: Int,
        year: Int
    ): Flow<DataState<List<TimeStats>>> = flow {
        emit(DataState.loading())

        val timeStats = getTimeStats(token, month, year)
        delay(300)

        emit(DataState.success(timeStats))
    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getTimeStats(
        token: String,
        month: Int,
        year: Int
    ): List<TimeStats> {
        return mapper.toDomainList(
            service.getStats(token = token, month = month, year = year)
        )
    }
}