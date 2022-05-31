package com.example.eplan.network.services

import com.example.eplan.network.model.VacationRequestDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VacationRequestService {
    @POST("richiediFerie")
    suspend fun vacationRequest(
        @Header(HEADER_AUTH) token: String,
        @Body vacationRequestDto: VacationRequestDto
    )
}