package com.example.eplan.network.services

import com.example.eplan.network.model.ActivityDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.GET
import retrofit2.http.Header

interface ActivityService {

    @GET("attivita")
    suspend fun getUserActivities(
        @Header(HEADER_AUTH) token: String
    ): List<ActivityDto>
}