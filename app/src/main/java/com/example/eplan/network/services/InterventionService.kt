package com.example.eplan.network.services

import com.example.eplan.network.model.InterventionDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.*

interface InterventionService {

    @GET("interventi")
    suspend fun getDayActivities(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<InterventionDto>

    @GET("interventi")
    suspend fun getActivity(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): InterventionDto


    @POST("saveInterventi")
    suspend fun updateActivity(
        @Header(HEADER_AUTH) token: String,
        @Body interventionDto: InterventionDto
    )

    @DELETE("delete")
    suspend fun deleteActivity(
        @Header(HEADER_AUTH) token: String,
        @Query("activityId") id: String
    )
}