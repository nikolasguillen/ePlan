package com.example.eplan.di

import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.services.LoginService
import com.example.eplan.network.services.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.example.eplan.network.services.AppointmentService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val URL = "https://gest.eplanweb.com/api/"

    /** Providers per interventi **/

    @Singleton
    @Provides
    fun provideWorkActivityDtoMapper(): WorkActivityDtoMapper {
        return WorkActivityDtoMapper()
    }

    @Singleton
    @Provides
    fun provideWorkActivityService(): WorkActivityService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WorkActivityService::class.java)
    }

    /** Providers per login **/

    @Singleton
    @Provides
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LoginService::class.java)
    }

    /** Providers per appuntamenti **/

    @Singleton
    @Provides
    fun provideAppointmentDtoMapper(): AppointmentDtoMapper {
        return AppointmentDtoMapper()
    }

    @Singleton
    @Provides
    fun provideAppointmentService(): AppointmentService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AppointmentService::class.java)
    }
}