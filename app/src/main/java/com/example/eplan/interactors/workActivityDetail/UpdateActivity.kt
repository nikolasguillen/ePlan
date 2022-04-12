package com.example.eplan.interactors.workActivityDetail

import android.util.Log
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.toJson
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class UpdateActivity
@Inject
constructor(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) {
    fun execute(
        token: String,
        workActivity: WorkActivity
    ): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.loading())

            //TODO controlla che ci sia connessione internet
            sendWorkActivity(token = token, workActivity = workActivity)

            delay(3000)

            emit(DataState.success(true))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Errore sconosciuto"))
        }
    }

    private suspend fun sendWorkActivity(
        token: String,
        workActivity: WorkActivity
    ) {
        service.updateActivity(
            token = token,
            workActivityDto = mapper.mapFromDomainModel(workActivity)
        )
    }
}