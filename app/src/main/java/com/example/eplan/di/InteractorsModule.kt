package com.example.eplan.di

import com.example.eplan.cache.UserDao
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentDetail.GetAppointmentById
import com.example.eplan.interactors.appointmentDetail.UpdateAppointment
import com.example.eplan.interactors.appointmentList.DayChangeAppointment
import com.example.eplan.interactors.camera.SaveProfilePicUri
import com.example.eplan.interactors.login.GetCredentialsFromCache
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.interactors.interventionDetail.GetInterventionById
import com.example.eplan.interactors.interventionDetail.UpdateIntervention
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.interactors.interventionList.DayChangeIntervention
import com.example.eplan.interactors.workActivityDetail.ValidateActivityId
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.model.InterventionDtoMapper
import com.example.eplan.network.services.AppointmentService
import com.example.eplan.network.services.LoginService
import com.example.eplan.network.services.InterventionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    /** Provider degli interactors degli interventi **/

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

    @ViewModelScoped
    @Provides
    fun provideActivityById(
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

    /** Provider dei validatori delle form **/

    @ViewModelScoped
    @Provides
    fun provideValidateActivityId(): ValidateActivityId {
        return ValidateActivityId()

    }
    @ViewModelScoped
    @Provides
    fun provideValidateDescription(): ValidateDescription {
        return ValidateDescription()
    }

    @ViewModelScoped
    @Provides
    fun provideValidateTime(): ValidateTime {
        return ValidateTime()
    }

    /** Interactors login **/

    @ViewModelScoped
    @Provides
    fun provideLoginResponse(
        service: LoginService,
        userDao: UserDao
    ): LoginAttempt {
        return LoginAttempt(
            service = service,
            userDao = userDao
        )
    }

    /** Provider dello user token **/

    @ViewModelScoped
    @Provides
    fun provideUserToken(
        userDao: UserDao
    ): GetToken {
        return GetToken(userDao = userDao)
    }

    /** Provider immagine profilo TODO cancellarli **/
    @ViewModelScoped
    @Provides
    fun provideSaveProfilePicUri(
        userDao: UserDao
    ): SaveProfilePicUri {
        return SaveProfilePicUri(userDao = userDao)
    }

    @ViewModelScoped
    @Provides
    fun provideGetProfilePicUri(
        userDao: UserDao
    ): GetProfilePicUri {
        return GetProfilePicUri(userDao = userDao)
    }

    @ViewModelScoped
    @Provides
    fun provideGetCredentialsFromCache(
        userDao: UserDao
    ): GetCredentialsFromCache {
        return GetCredentialsFromCache(userDao = userDao)
    }

    /** Provider degli interactors degli appuntamenti **/

    @ViewModelScoped
    @Provides
    fun provideGetAppointmentById(
        service: AppointmentService,
        mapper: AppointmentDtoMapper
    ): GetAppointmentById {
        return GetAppointmentById(service = service, mapper = mapper)
    }

    @ViewModelScoped
    @Provides
    fun provideUpdateAppointment(
        service: AppointmentService,
        mapper: AppointmentDtoMapper
    ): UpdateAppointment {
        return UpdateAppointment(service = service, mapper = mapper)
    }

    @ViewModelScoped
    @Provides
    fun provideDayChangeAppointment(
        service: AppointmentService,
        mapper: AppointmentDtoMapper
    ): DayChangeAppointment {
        return DayChangeAppointment(service = service, mapper = mapper)
    }
}