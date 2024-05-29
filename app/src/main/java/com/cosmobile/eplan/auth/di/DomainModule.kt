package com.cosmobile.eplan.auth.di

import android.content.SharedPreferences
import com.cosmobile.eplan.auth.data.services.LoginService
import com.cosmobile.eplan.auth.domain.use_case.LoginAttempt
import com.cosmobile.eplan.auth.domain.use_case.SaveUserData
import com.cosmobile.eplan.core.domain.preferences.Preferences
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
    fun provideLoginResponse(
        service: LoginService,
        preferences: Preferences
    ): LoginAttempt {
        return LoginAttempt(
            service = service,
            encryptedPreferences = preferences
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSaveUserData(
        preferences: SharedPreferences
    ): SaveUserData {
        return SaveUserData(sharedPreferences = preferences)
    }
}