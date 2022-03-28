package com.example.eplan.repository

import com.example.eplan.domain.model.User
import com.example.eplan.network.model.UserDtoMapper
import com.example.eplan.network.model.UserService

class UserRepositoryImpl(
    private val service: UserService,
    private val mapper: UserDtoMapper
): UserRepository {

    override suspend fun getPeople(): List<User> {
        return mapper.toDomainList(service.getUsers("TODO", "TODO").users)
    }
}