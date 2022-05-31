package com.example.eplan.network.services

import com.example.eplan.network.model.TimeStatsDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TimeStatsService {
    @GET("getStats")
    suspend fun getStats(
        @Header(HEADER_AUTH) token: String,
        @Query("mese") month: Int,
        @Query("year") year: Int
    ): List<TimeStatsDto>
}