package com.cosmobile.eplan.appointments_list.di

import com.cosmobile.eplan.appointments_list.data.services.AppointmentsListService
import com.cosmobile.eplan.core.data.URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideAppointmentsListService(client: OkHttpClient): AppointmentsListService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(AppointmentsListService::class.java)
    }
}