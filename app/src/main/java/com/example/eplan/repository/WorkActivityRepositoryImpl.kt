package com.example.eplan.repository

import android.content.Context
import android.util.Log
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.presentation.util.TAG
import com.google.gson.Gson
import java.io.IOException
import java.time.LocalDate

class WorkActivityRepositoryImpl(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) : WorkActivityRepository {

    override suspend fun getDayActivities(
        userToken: String,
        query: LocalDate,
        context: Context
    ): List<WorkActivity> {

        lateinit var tracciatoJson: String
        try {
            tracciatoJson = context.assets.open("tracciatoEplan.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
        }

        val gson = Gson()
        val workActivities = mapper.toDomainList(
            gson.fromJson(
                tracciatoJson,
                Array<WorkActivityDto>::class.java
            ).asList()
        )

        /*return workActivities.filter { workActivity ->
            workActivity.date.dayOfMonth == dayOfMonth && workActivity.date.month == Month.of(month)
        }*/

        return workActivities.filter { it.date == query }
    }

    override suspend fun getActivityById(
        userToken: String,
        activityId: String,
        context: Context
    ): WorkActivity {
        /*return mapper.mapToDomainModel(service.getActivity(userToken, activityId).workActivity)*/


        lateinit var tracciatoJson: String
        try {
            tracciatoJson = context.assets.open("tracciatoEplan.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
        }

        val gson = Gson()
        val workActivities = mapper.toDomainList(
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

    override suspend fun updateWorkActivity(
        userToken: String,
        workActivity: WorkActivity
    ) {
        service.updateActivity(userToken, workActivity.id, "TODO")
        /*TODO come mando l'attività?*/
    }

    override suspend fun deleteWorkActivity(userToken: String, id: String) {
        service.deleteActivity(userToken, id)
    }
}