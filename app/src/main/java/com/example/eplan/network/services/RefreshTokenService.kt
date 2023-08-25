package com.example.eplan.network.services

import com.example.eplan.network.responses.RefreshTokenResponse
import com.example.eplan.network.util.HEADER_AUTH
import retrofit2.http.GET
import retrofit2.http.Header

interface RefreshTokenService {

    @GET("me")
    suspend fun getNewToken(
        @Header(HEADER_AUTH) token: String
    ): RefreshTokenResponse
}