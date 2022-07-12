package com.example.eplan.di

import com.example.eplan.network.model.ActivityDtoMapper
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.model.InterventionDtoMapper
import com.example.eplan.network.model.TimeStatsDtoMapper
import com.example.eplan.network.services.*
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

    /* Providers per interventi */

    @Singleton
    @Provides
    fun provideInterventionDtoMapper(): InterventionDtoMapper {
        return InterventionDtoMapper()
    }

    @Singleton
    @Provides
    fun provideInterventionService(): InterventionService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(InterventionService::class.java)
    }

    /* Providers per login */

    @Singleton
    @Provides
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LoginService::class.java)
    }

    /* Providers per appuntamenti */

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

    /* Provider per statistiche */

    @Singleton
    @Provides
    fun provideTimeStatsService(): TimeStatsService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(TimeStatsService::class.java)
    }

    @Singleton
    @Provides
    fun provideTimeStatsDtoMapper(): TimeStatsDtoMapper {
        return TimeStatsDtoMapper()
    }

    /* Provider per le attività dell'utente */

    @Singleton
    @Provides
    fun provideActivityService(): ActivityService {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ActivityService::class.java)
    }

    @Singleton
    @Provides
    fun provideActivityDtoMapper(): ActivityDtoMapper {
        return ActivityDtoMapper()
    }
}