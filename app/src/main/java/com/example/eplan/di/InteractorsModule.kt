package com.example.eplan.di

import com.example.eplan.cache.UserDao
import com.example.eplan.interactors.GetProfilePicUri
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.camera.SaveProfilePicUri
import com.example.eplan.interactors.login.GetCredentialsFromCache
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.interactors.workActivityDetail.GetActivityById
import com.example.eplan.interactors.workActivityDetail.UpdateActivity
import com.example.eplan.interactors.workActivityDetail.ValidateDescription
import com.example.eplan.interactors.workActivityDetail.ValidateTime
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
        service: LoginService,
        userDao: UserDao
    ): LoginAttempt {
        return LoginAttempt(
            service = service,
            userDao = userDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideActivityById(
        service: WorkActivityService,
        mapper: WorkActivityDtoMapper
    ): GetActivityById {
        return GetActivityById(
            service = service,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSubmitWorkActivity(
        service: WorkActivityService,
        mapper: WorkActivityDtoMapper
    ): UpdateActivity {
        return UpdateActivity(
            service = service,
            mapper = mapper
        )
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

    @ViewModelScoped
    @Provides
    fun provideUserToken(
        userDao: UserDao
    ): GetToken {
        return GetToken(userDao = userDao)
    }

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
}