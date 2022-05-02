package com.example.eplan.network.services

import com.example.eplan.network.model.WorkActivityDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.*

interface WorkActivityService {

    @GET("interventi")
    suspend fun getDayActivities(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<WorkActivityDto>

    @GET("interventi")
    suspend fun getActivity(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): WorkActivityDto


    @POST("saveInterventi")
    suspend fun updateActivity(
        @Header(HEADER_AUTH) token: String,
        @Body workActivityDto: WorkActivityDto
    )

    @DELETE("delete")
    suspend fun deleteActivity(
        @Header(HEADER_AUTH) token: String,
        @Query("activityId") id: String
    )
}