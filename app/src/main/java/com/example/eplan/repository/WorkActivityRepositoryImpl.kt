package com.example.eplan.repository

import android.content.Context
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException

class WorkActivityRepositoryImpl(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) : WorkActivityRepository {

    override suspend fun getDayActivities(
        userToken: String,
        query: String,
        context: Context
    ): List<WorkActivity> {

        val workActivityDtoMapper = WorkActivityDtoMapper()

        lateinit var tracciatoJson: String
        try {
            tracciatoJson = context.assets.open("tracciatoEplan.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
        }

        val gson = Gson()
        val workActivities = workActivityDtoMapper.toDomainList(
            gson.fromJson(
                tracciatoJson,
                Array<WorkActivityDto>::class.java
            ).asList()
        )

        /*return workActivities.filter { workActivity ->
            workActivity.date.dayOfMonth == dayOfMonth && workActivity.date.month == Month.of(month)
        }*/

        return workActivities
    }

    override suspend fun getActivityById(userToken: String, activityId: String, context: Context): WorkActivity {
        /*return mapper.mapToDomainModel(service.getActivity(userToken, activityId).workActivity)*/

        val workActivityDtoMapper = WorkActivityDtoMapper()

        lateinit var tracciatoJson: String
        try {
            tracciatoJson = context.assets.open("tracciatoEplan.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
        }

        val gson = Gson()
        val workActivities = workActivityDtoMapper.toDomainList(
            gson.fromJson(
                tracciatoJson,
                Array<WorkActivityDto>::class.java
            ).asList()
        )

        /*return workActivities.filter { workActivity ->
            workActivity.date.dayOfMonth == dayOfMonth && workActivity.date.month == Month.of(month)
        }*/

        return workActivities.first { it.id == activityId }
    }

    override suspend fun updateWorkActivity(userToken: String, workActivity: WorkActivity) {
        service.updateActivity(userToken, workActivity.id, "TODO")
        /*TODO come mando l'attività?*/
    }

    override suspend fun deleteWorkActivity(userToken: String, id: String) {
        service.deleteActivity(userToken, id)
    }
}