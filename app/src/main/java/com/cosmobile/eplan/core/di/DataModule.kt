package com.cosmobile.eplan.core.di

import com.cosmobile.eplan.core.data.URL
import com.cosmobile.eplan.core.data.model.ActivityDtoMapper
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.model.WarningTimeConverterImpl
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.core.data.services.RefreshTokenService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideInterventionDtoMapper(): InterventionDtoMapper {
        return InterventionDtoMapper()
    }

    @Singleton
    @Provides
    fun provideAppointmentDtoMapper(): AppointmentDtoMapper {
        return AppointmentDtoMapper(WarningTimeConverterImpl())
    }

    @Singleton
    @Provides
    fun provideActivityService(client: OkHttpClient): ActivityService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(ActivityService::class.java)
    }

    @Singleton
    @Provides
    fun provideActivityDtoMapper(): ActivityDtoMapper {
        return ActivityDtoMapper()
    }

    @Singleton
    @Provides
    fun provideRefreshTokenService(client: OkHttpClient): RefreshTokenService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(RefreshTokenService::class.java)
    }

    @Singleton
    @Provides
    fun provideInterventionService(client: OkHttpClient): InterventionService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
            .create(InterventionService::class.java)
    }
}