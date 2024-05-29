package com.cosmobile.eplan.appointments_list.di

import com.cosmobile.eplan.appointments_list.data.services.AppointmentsListService
import com.cosmobile.eplan.appointments_list.domain.use_cases.DayChangeAppointment
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
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
    fun provideDayChangeAppointment(
        appointmentsListService: AppointmentsListService,
        activityService: ActivityService,
        mapper: AppointmentDtoMapper
    ): DayChangeAppointment {
        return DayChangeAppointment(
            appointmentsListService = appointmentsListService,
            activitiesService = activityService,
            mapper = mapper
        )
    }
}