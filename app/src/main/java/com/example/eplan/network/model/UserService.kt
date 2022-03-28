package com.example.eplan.network.model

import com.example.eplan.network.responses.UsersListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserService {

    @GET("get")
    suspend fun getUsers(
        @Header("UserToken") token: String,
        @Query("users") users: String
    ): UsersListResponse
}