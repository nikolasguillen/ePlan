package com.cosmobile.eplan.vacation_request.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.vacation_request.data.model.VacationRequestDto
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