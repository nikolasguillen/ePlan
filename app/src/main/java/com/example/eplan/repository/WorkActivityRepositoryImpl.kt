package com.example.eplan.repository

import android.content.Context
import android.util.Log
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import java.io.IOException

class WorkActivityRepositoryImpl(
    private val workActivityService: WorkActivityService,
    private val mapper: WorkActivityDtoMapper
) : WorkActivityRepository {

    override suspend fun getMonthActivities(
        userToken: String,
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
        val workActivities =
            gson.fromJson(tracciatoJson, Array<WorkActivityDto>::class.java).asList()
        return workActivityDtoMapper.toDomainList(workActivities)
    }
    /*override suspend fun getMonthActivities(
        userToken: String,
        month: Int
    ): List<WorkActivity> {

        return mapper.toDomainList(workActivityService.getMonthActivities(userToken, month).workActivities)
    }*/
}