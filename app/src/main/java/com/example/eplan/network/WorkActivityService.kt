package com.example.eplan.network

import com.example.eplan.network.responses.WorkActivityByIdResponse
import com.example.eplan.network.responses.WorkActivityDayResponse
import retrofit2.http.*

interface WorkActivityService {

    @GET("getByDay")
    suspend fun getDayActivities(
        @Header("userToken") token: String,
        @Query("query") query: String
    ): WorkActivityDayResponse

    @GET("getById")
    suspend fun getActivityById(
        @Header("userToken") token: String,
        @Query("id") id: String
    ): WorkActivityByIdResponse


    @GET("update")
    suspend fun updateActivity(
        @Header("userToken") token: String,
        @Query("activityId") id: String,
        @Query("updatedActivity") activity: String
    )
}