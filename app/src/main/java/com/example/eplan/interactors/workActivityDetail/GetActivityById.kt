package com.example.eplan.interactors.workActivityDetail

import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.services.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetActivityById
@Inject
constructor(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) {
    fun execute(
        token: String,
        id: String
    ): Flow<DataState<WorkActivity>> = flow {
        try {
            emit(DataState.loading())

            // TODO controlla che ci sia connessione a internet
            val workActivity = getWorkActivityFromNetwork(token = token, id = id)
            delay(300)

            emit(DataState.success(workActivity))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun getWorkActivityFromNetwork(
        token: String,
        id: String
    ): WorkActivity {
        return mapper.mapToDomainModel(
            service.getActivity(
                token = token,
                id = id
            )
        )
    }
}