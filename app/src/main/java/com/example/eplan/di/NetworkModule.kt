package com.example.eplan.di

import com.example.eplan.network.LoginService
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.UserDtoMapper
import com.example.eplan.network.model.UserService
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

    var auth_token = "Bearer "

    @Singleton
    @Provides
    fun provideWorkActivityDtoMapper(): WorkActivityDtoMapper {
        return WorkActivityDtoMapper()
    }

    @Singleton
    @Provides
    fun provideWorkActivityService(): WorkActivityService {
        return Retrofit.Builder()
            .baseUrl("https://gest.eplanweb.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WorkActivityService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl("https://gest.eplanweb.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserDtoMapper(): UserDtoMapper {
        return UserDtoMapper()
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService {
        return Retrofit.Builder()
            .baseUrl("https://ciao.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(UserService::class.java)
    }

    @Singleton
    @Provides
    @Named("auth_token")
    fun provideUserToken(): String {
        return auth_token
    }
}