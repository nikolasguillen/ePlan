package com.example.eplan.network

import com.example.eplan.network.responses.WorkActivityMonthResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WorkActivityService {

    @GET("get")
    suspend fun getMonthActivities(
        @Header("UserToken") token: String,
        @Query("month") month: Int
    ): WorkActivityMonthResponse
}