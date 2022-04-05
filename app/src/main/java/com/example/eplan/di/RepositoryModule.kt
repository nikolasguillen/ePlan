package com.example.eplan.di

import com.example.eplan.network.LoginService
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.UserDtoMapper
import com.example.eplan.network.model.UserService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWorkActivityRepository(
        workActivityService: WorkActivityService,
        workActivityDtoMapper: WorkActivityDtoMapper
    ): WorkActivityRepository {
        return WorkActivityRepositoryImpl(workActivityService, workActivityDtoMapper)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        userService: UserService,
        userDtoMapper: UserDtoMapper
    ): UserRepository {
        return UserRepositoryImpl(userService, userDtoMapper)
    }
}