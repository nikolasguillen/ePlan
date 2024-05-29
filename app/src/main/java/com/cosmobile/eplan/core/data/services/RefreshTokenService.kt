package com.cosmobile.eplan.core.data.services

import com.cosmobile.eplan.core.data.HEADER_AUTH
import com.cosmobile.eplan.core.data.model.RefreshTokenResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface RefreshTokenService {

    @GET("me")
    suspend fun getNewToken(
        @Header(HEADER_AUTH) token: String
    ): RefreshTokenResponse
}