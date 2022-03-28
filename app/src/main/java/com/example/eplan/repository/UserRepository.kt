package com.example.eplan.repository

import com.example.eplan.domain.model.User

interface UserRepository {

    suspend fun getPeople(): List<User>
}