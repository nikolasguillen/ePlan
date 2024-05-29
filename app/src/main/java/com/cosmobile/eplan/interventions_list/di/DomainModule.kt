package com.cosmobile.eplan.interventions_list.di

import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.interventions_list.domain.use_cases.DayChangeIntervention
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
    fun provideDayActivities(
        service: InterventionService,
        mapper: InterventionDtoMapper
    ): DayChangeIntervention {
        return DayChangeIntervention(
            service = service,
            mapper = mapper
        )
    }
}