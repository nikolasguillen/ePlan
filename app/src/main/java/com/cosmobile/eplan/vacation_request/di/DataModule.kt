package com.cosmobile.eplan.vacation_request.di

import com.cosmobile.eplan.core.data.URL
import com.cosmobile.eplan.vacation_request.data.services.VacationRequestService
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
object DataModule {

    /* Provider per richiesta ferie */

    @Singleton
    @Provides
    fun provideVacationRequestService(): VacationRequestService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(VacationRequestService::class.java)
    }
}