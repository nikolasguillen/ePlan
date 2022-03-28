package com.example.eplan.di

import androidx.navigation.Navigator
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWorkActivityDtoMapper(): WorkActivityDtoMapper {
        return WorkActivityDtoMapper()
    }

    @Singleton
    @Provides
    fun provideWorkActivityService(): WorkActivityService {
        return Retrofit.Builder()
            .baseUrl("https://ciao.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WorkActivityService::class.java)
    }

    @Singleton
    @Provides
    @Named("auth_token")
    fun provideUserToken(): String {
        return "Token"
    }
}