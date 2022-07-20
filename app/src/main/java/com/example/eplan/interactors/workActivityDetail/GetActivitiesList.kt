package com.example.eplan.interactors.workActivityDetail

import android.util.Log
import com.example.eplan.domain.data.DataState
import com.example.eplan.domain.model.Activity
import com.example.eplan.network.model.ActivityDtoMapper
import com.example.eplan.network.services.ActivityService
import com.example.eplan.presentation.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetActivitiesList
@Inject
constructor(
    private val service: ActivityService,
    private val mapper: ActivityDtoMapper
) {
    fun execute(token: String): Flow<DataState<List<Activity>>> = flow {

        emit(DataState.loading())

        // TODO controlla che ci sia connessione a internet
        val activities = getActivitiesFromNetwork(token = token)

        emit(DataState.success(activities))

    }.catch { emit(DataState.error(it.message ?: "Errore sconosciuto")) }

    private suspend fun getActivitiesFromNetwork(
        token: String
    ): List<Activity> {
        return mapper.toDomainList(service.getUserActivities(token = token))
    }
}