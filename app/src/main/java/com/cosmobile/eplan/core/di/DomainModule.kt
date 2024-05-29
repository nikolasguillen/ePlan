package com.cosmobile.eplan.core.di

import android.content.SharedPreferences
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.domain.use_cases.ClearUserData
import com.cosmobile.eplan.auth.domain.use_case.RefreshToken
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateActivity
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateDescription
import com.cosmobile.eplan.core.domain.use_cases.validation.ValidateTime
import com.cosmobile.eplan.core.data.services.RefreshTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @ViewModelScoped
    @Provides
    fun provideUserToken(
        preferences: Preferences
    ): GetToken {
        return GetToken(preferences = preferences)
    }

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

    @ViewModelScoped
    @Provides
    fun provideRefreshToken(
        service: RefreshTokenService,
        encryptedPreferences: Preferences,
        sharedPreferences: SharedPreferences
    ): RefreshToken {
        return RefreshToken(
            service = service,
            encryptedPreferences = encryptedPreferences,
            sharedPreferences = sharedPreferences
        )
    }

    @ViewModelScoped
    @Provides
    fun provideClearUserData(
        encryptedPreferences: Preferences,
        sharedPreferences: SharedPreferences
    ): ClearUserData {
        return ClearUserData(
            encryptedPreferences = encryptedPreferences,
            sharedPreferences = sharedPreferences
        )
    }
}