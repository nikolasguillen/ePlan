package com.cosmobile.eplan.vacation_request.di

import com.cosmobile.eplan.vacation_request.domain.use_cases.RequestVacation
import com.cosmobile.eplan.vacation_request.data.services.VacationRequestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @ViewModelScoped
    @Provides
    fun provideRequestVacation(
        service: VacationRequestService
    ): RequestVacation {
        return RequestVacation(service = service)
    }
}