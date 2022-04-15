package com.example.eplan.di

import com.example.eplan.cache.UserDao
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.login.LoginAttempt
import com.example.eplan.interactors.workActivityDetail.GetById
import com.example.eplan.interactors.workActivityDetail.UpdateActivity
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
    ): GetById {
        return GetById(
            service = service,
            mapper = mapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSendWorkActivity(
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
    fun provideUserToken(
        userDao: UserDao
    ): GetToken {
        return GetToken(userDao = userDao)
    }
}