package com.cosmobile.eplan.appointment_detail.di

import com.cosmobile.eplan.appointment_detail.data.services.AppointmentDetailService
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
    fun provideAppointmentDetailService(client: OkHttpClient): AppointmentDetailService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(AppointmentDetailService::class.java)
    }
}