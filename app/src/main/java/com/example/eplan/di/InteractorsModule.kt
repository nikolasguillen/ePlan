package com.example.eplan.di

import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.interactors.workActivityList.DayChange
import com.example.eplan.network.LoginService
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.WorkActivityDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideDayActivities(
        service: WorkActivityService,
        mapper: WorkActivityDtoMapper
    ): DayChange {
        return DayChange(
            service = service,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideLoginResponse(
        service: LoginService
    ): LoginAttempt {
        return LoginAttempt(
            service = service
        )
    }
}