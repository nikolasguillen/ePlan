package com.example.eplan.repository

import android.content.Context
import com.example.eplan.domain.model.User
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.toJson
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.google.gson.Gson
import java.io.IOException
import java.time.Month

class WorkActivityRepositoryImpl(
    private val service: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) : WorkActivityRepository {

    override suspend fun getDayActivities(
        userToken: String,
        dayOfMonth: Int,
        month: Int,
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

        return workActivities.filter { workActivity ->
            workActivity.date.dayOfMonth == dayOfMonth && workActivity.date.month == Month.of(month)
        }
    }
    /*override suspend fun getMonthActivities(
        userToken: String,
        month: Int
    ): List<WorkActivity> {

        return mapper.toDomainList(workActivityService.getMonthActivities(userToken, month).workActivities)
    }*/

    override suspend fun getActivityById(userToken: String, activityId: Int): WorkActivity {
        return mapper.mapToDomainModel(service.getActivityById(userToken, activityId).workActivity)
    }

    override suspend fun updateWorkActivity(userToken: String, workActivity: WorkActivity) {
        service.updateActivity(userToken, workActivity.id, "TODO")
    }
}