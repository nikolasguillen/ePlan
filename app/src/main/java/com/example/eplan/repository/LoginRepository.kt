package com.example.eplan.repository

interface LoginRepository {

    suspend fun login(username: String, password: String): String
}