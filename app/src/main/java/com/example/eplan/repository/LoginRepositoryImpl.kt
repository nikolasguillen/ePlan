package com.example.eplan.repository

import com.example.eplan.network.LoginService

class LoginRepositoryImpl(private val service: LoginService) : LoginRepository {

    override suspend fun login(username: String, password: String): String {
        return service.getUserToken(username = username, password = password)
    }

}