package com.example.eplan.di

import com.example.eplan.domain.preferences.Preferences
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.appointmentDetail.GetAppointmentById
import com.example.eplan.interactors.appointmentDetail.UpdateAppointment
import com.example.eplan.interactors.appointmentList.DayChangeAppointment
import com.example.eplan.interactors.camera.SaveProfilePicUri
import com.example.eplan.interactors.interventionDetail.GetInterventionById
import com.example.eplan.interactors.interventionDetail.UpdateIntervention
import com.example.eplan.interactors.interventionList.DayChangeIntervention
import com.example.eplan.interactors.login.GetCredentialsFromCache
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.interactors.timeStats.GetTimeStats
import com.example.eplan.interactors.vacationRequest.RequestVacation
import com.example.eplan.interactors.workActivityDetail.GetActivitiesList
import com.example.eplan.interactors.workActivityDetail.ValidateActivity
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
import com.example.eplan.network.model.ActivityDtoMapper
import com.example.eplan.network.model.AppointmentDtoMapper
import com.example.eplan.network.model.InterventionDtoMapper
import com.example.eplan.network.model.TimeStatsDtoMapper
import com.example.eplan.network.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    /* Provider degli interactors degli interventi */

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

    /* Provider dei validatori delle form */

    @ViewModelScoped
    @Provides
    fun provideValidateActivityId(): ValidateActivity {
        return ValidateActivity()

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

    /* Interactors login */

    @ViewModelScoped
    @Provides
    fun provideLoginResponse(
        service: LoginService,
        preferences: Preferences
    ): LoginAttempt {
        return LoginAttempt(
            service = service,
            preferences = preferences
        )
    }

    /* Provider dello user token */

    @ViewModelScoped
    @Provides
    fun provideUserToken(
        preferences: Preferences
    ): GetToken {
        return GetToken(preferences = preferences)
    }

    /* Provider immagine profilo TODO cancellarli **/
    @ViewModelScoped
    @Provides
    fun provideSaveProfilePicUri(
        preferences: Preferences
    ): SaveProfilePicUri {
        return SaveProfilePicUri(preferences = preferences)
    }

    @ViewModelScoped
    @Provides
    fun provideGetProfilePicUri(
        preferences: Preferences
    ): GetProfilePicUri {
        return GetProfilePicUri(preferences = preferences)
    }

    @ViewModelScoped
    @Provides
    fun provideGetCredentialsFromCache(
        preferences: Preferences
    ): GetCredentialsFromCache {
        return GetCredentialsFromCache(preferences = preferences)
    }

    /* Provider degli interactors degli appuntamenti */

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

    /* Provider degli interactors della richiesta ferie */

    @ViewModelScoped
    @Provides
    fun provideRequestVacation(
        service: VacationRequestService
    ): RequestVacation {
        return RequestVacation(service = service)
    }

    /* Provider degli interactors delle statistiche */

    @ViewModelScoped
    @Provides
    fun provideGetTimeStats(
        service: TimeStatsService,
        mapper: TimeStatsDtoMapper
    ): GetTimeStats {
        return GetTimeStats(service = service, mapper = mapper)
    }

    /* Provider degli interactors delle attività */

    @ViewModelScoped
    @Provides
    fun provideGetActivitiesList(
        service: ActivityService,
        mapper: ActivityDtoMapper
    ): GetActivitiesList {
        return GetActivitiesList(service = service, mapper = mapper)
    }
}