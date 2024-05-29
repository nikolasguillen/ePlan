package com.cosmobile.eplan.appointment_detail.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.core.data.model.AppointmentListItemDto
import com.cosmobile.eplan.intervention_detail.data.model.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AppointmentDetailService {
    @GET("appuntamenti")
    suspend fun getAppointment(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): AppointmentListItemDto

    @GET("appuntamenti")
    suspend fun getAppointments(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<AppointmentListItemDto>

    @POST("saveAppuntamenti")
    suspend fun updateAppointment(
        @Header(HEADER_AUTH) token: String,
        @Body appointmentListItemDto: AppointmentListItemDto
    )

    @DELETE("deleteAppuntamenti")
    suspend fun deleteAppointment(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): DeleteResponse
}