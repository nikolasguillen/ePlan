package com.cosmobile.eplan.intervention_detail.di

import com.cosmobile.eplan.core.data.model.ActivityDtoMapper
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.intervention_detail.domain.use_cases.DeleteIntervention
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetActivitiesList
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetDescriptionSuggestions
import com.cosmobile.eplan.intervention_detail.domain.use_cases.GetInterventionById
import com.cosmobile.eplan.intervention_detail.domain.use_cases.UpdateIntervention
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
    fun provideInterventionById(
        service: InterventionService,
        mapper: InterventionDtoMapper
    ): GetInterventionById {
        return GetInterventionById(
            service = service,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSubmitIntervention(
        service: InterventionService,
        mapper: InterventionDtoMapper
    ): UpdateIntervention {
        return UpdateIntervention(
            service = service,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideDeleteIntervention(
        service: InterventionService
    ): DeleteIntervention {
        return DeleteIntervention(
            service = service
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetActivitiesList(
        activityService: ActivityService,
        interventionService: InterventionService,
        activityDtoMapper: ActivityDtoMapper,
        interventionDtoMapper: InterventionDtoMapper
    ): GetActivitiesList {
        return GetActivitiesList(
            activityService = activityService,
            interventionService = interventionService,
            activityMapper = activityDtoMapper,
            interventionMapper = interventionDtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetDescriptionSuggestions(
        service: InterventionService,
        mapper: InterventionDtoMapper
    ): GetDescriptionSuggestions {
        return GetDescriptionSuggestions(service = service, mapper = mapper)
    }
}