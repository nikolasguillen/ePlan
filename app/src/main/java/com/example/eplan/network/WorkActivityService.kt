package com.example.eplan.network

import com.example.eplan.network.model.WorkActivityDto
import retrofit2.http.*

interface WorkActivityService {

    @GET("interventi")
    suspend fun getDayActivities(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<WorkActivityDto>

    @GET("interventi")
    suspend fun getActivity(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): WorkActivityDto


    @GET("update")
    suspend fun updateActivity(
        @Header("Authorization") token: String,
        @Query("activityId") id: String,
        @Query("updatedActivity") activity: String
    )

    @DELETE("delete")
    suspend fun deleteActivity(
        @Header("Authorization") token: String,
        @Query("activityId") id: String
    )
}