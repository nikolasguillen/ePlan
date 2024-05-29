package com.cosmobile.eplan.appointment_detail.di

import com.cosmobile.eplan.appointment_detail.data.services.AppointmentDetailService
import com.cosmobile.eplan.appointment_detail.domain.use_cases.DeleteAppointment
import com.cosmobile.eplan.appointment_detail.domain.use_cases.GetAppointmentById
import com.cosmobile.eplan.appointment_detail.domain.use_cases.UpdateAppointment
import com.cosmobile.eplan.core.data.model.AppointmentDtoMapper
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
    fun provideGetAppointmentById(
        appointmentDetailService: AppointmentDetailService,
        mapper: AppointmentDtoMapper
    ): GetAppointmentById {
        return GetAppointmentById(
            appointmentDetailService = appointmentDetailService,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideUpdateAppointment(
        service: AppointmentDetailService,
        mapper: AppointmentDtoMapper
    ): UpdateAppointment {
        return UpdateAppointment(service = service, mapper = mapper)
    }

    @ViewModelScoped
    @Provides
    fun provideDeleteAppointment(
        service: AppointmentDetailService
    ): DeleteAppointment {
        return DeleteAppointment(
            service = service
        )
    }
}