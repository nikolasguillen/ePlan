package com.cosmobile.eplan.time_stats.di

import android.content.SharedPreferences
import com.cosmobile.eplan.core.data.model.ActivityDtoMapper
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.time_stats.domain.use_cases.GetInterventionsForMonth
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
    fun provideGetTimeStats(
        interventionService: InterventionService,
        interventionDtoMapper: InterventionDtoMapper,
        activityService: ActivityService,
        activityDtoMapper: ActivityDtoMapper,
        sharedPreferences: SharedPreferences
    ): GetInterventionsForMonth {
        return GetInterventionsForMonth(
            interventionService = interventionService,
            interventionDtoMapper = interventionDtoMapper,
            activityService = activityService,
            activityDtoMapper = activityDtoMapper,
            sharedPreferences = sharedPreferences
        )
    }
}