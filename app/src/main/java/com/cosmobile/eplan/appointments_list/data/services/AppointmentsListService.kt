package com.cosmobile.eplan.appointments_list.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.core.data.model.AppointmentListItemDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AppointmentsListService {
    @GET("appuntamenti")
    suspend fun getDayAppointments(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<AppointmentListItemDto>
}