package com.cosmobile.eplan.auth.data.services

import com.cosmobile.eplan.auth.data.model.CredentialsDto
import com.cosmobile.eplan.auth.data.model.LoginResponse
import retrofit2.http.*

interface LoginService {

    @POST("login")
    suspend fun login(@Body credentials: CredentialsDto): LoginResponse
}
