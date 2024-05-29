package com.cosmobile.eplan.core.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.core.data.model.ActivityDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ActivityService {

    @GET("attivita")
    suspend fun getUserActivities(
        @Header(HEADER_AUTH) token: String
    ): List<ActivityDto>
}