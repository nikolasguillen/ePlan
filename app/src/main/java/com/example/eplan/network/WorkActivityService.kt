package com.example.eplan.network

import com.example.eplan.network.responses.WorkActivityByIdResponse
import com.example.eplan.network.responses.WorkActivityDayResponse
import retrofit2.http.*

interface WorkActivityService {

    @GET("get")
    suspend fun getDayActivities(
        @Header("userToken") token: String,
        @Query("dayOfMonth") dayOfMonth: Int,
        @Query("month") month: Int
    ): WorkActivityDayResponse

    @GET("get")
    suspend fun getActivityById(
        @Header("userToken") token: String,
        @Query("id") id: Int
    ): WorkActivityByIdResponse


    @POST("post")
    suspend fun updateActivity(
        @Header("userToken") token: String,
        @Query("activityId") id: Int,
        @Query("updatedActivity") activity: String
    )
}