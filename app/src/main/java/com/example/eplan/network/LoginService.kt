package com.example.eplan.network

import com.example.eplan.network.responses.WorkActivityDayResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LoginService {

    @GET("get")
    suspend fun getUserToken(
        @Query("username") username: String,
        @Query("password") password: String
    ): String

}
