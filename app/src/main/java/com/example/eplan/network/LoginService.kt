package com.example.eplan.network

import com.example.eplan.network.model.CredentialsDto
import com.example.eplan.network.responses.LoginResponse
import retrofit2.http.*

interface LoginService {

    @POST("login")
    suspend fun login(@Body credentials: CredentialsDto): LoginResponse
}
