package com.example.eplan.network.model

import com.example.eplan.domain.model.User
import com.example.eplan.domain.util.DomainMapper

class UserDtoMapper: DomainMapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User {
        return User(model.fullName)
    }

    override fun mapFromDomainModel(domainModel: User): UserDto {
        return UserDto(domainModel.fullName)
    }

    fun toDomainList(initial: List<UserDto>): List<User> {
        return initial.map { mapToDomainModel(it) }
    }
}