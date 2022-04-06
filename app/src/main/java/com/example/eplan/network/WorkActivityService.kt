package com.example.eplan.network

import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.responses.WorkActivityByIdResponse
import com.example.eplan.network.responses.WorkActivityDayResponse
import retrofit2.http.*

interface WorkActivityService {

    @GET("interventi")
    suspend fun getDayActivities(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<WorkActivityDto>

    @GET("getById")
    suspend fun getActivity(
        @Header("userToken") token: String,
        @Query("id") id: String
    ): WorkActivityByIdResponse


    @GET("update")
    suspend fun updateActivity(
        @Header("userToken") token: String,
        @Query("activityId") id: String,
        @Query("updatedActivity") activity: String
    )

    @DELETE("delete")
    suspend fun deleteActivity(
        @Header("userToken") token: String,
        @Query("activityId") id: String
    )
}