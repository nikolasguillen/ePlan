package com.example.eplan.network.services

import com.example.eplan.network.model.AppointmentDto
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.*

interface AppointmentService {

    @GET("appuntamenti")
    suspend fun getDayAppointments(
        @Header(HEADER_AUTH) token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<AppointmentDto>

    @GET("appuntamenti")
    suspend fun getAppointment(
        @Header(HEADER_AUTH) token: String,
        @Query("id") id: String
    ): AppointmentDto

    @POST("saveAppuntamenti")
    suspend fun updateAppointment(
        @Header(HEADER_AUTH) token: String,
        @Body appointmentDto: AppointmentDto
    )
}