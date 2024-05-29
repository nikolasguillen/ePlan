package com.cosmobile.eplan.core.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.core.data.model.InterventionDto
import com.cosmobile.eplan.intervention_detail.data.model.DeleteResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface InterventionService {
    @GET("interventi")
    suspend fun getPeriodInterventions(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<InterventionDto>

    @GET("interventi")
    suspend fun getIntervention(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): InterventionDto

    @POST("saveInterventi")
    suspend fun updateIntervention(
        @Header(HEADER_AUTH) token: String,
        @Body interventionDto: InterventionDto
    )

    @DELETE("deleteInterventi")
    suspend fun deleteIntervention(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): DeleteResponse
}