package com.example.eplan.di

import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.repository.WorkActivityRepository
import com.example.eplan.repository.WorkActivityRepositoryImpl
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
}