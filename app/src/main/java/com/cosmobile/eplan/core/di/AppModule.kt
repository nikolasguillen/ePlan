package com.cosmobile.eplan.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cosmobile.eplan.core.domain.preferences.DefaultPreferences
import com.cosmobile.eplan.core.domain.preferences.Preferences
import com.cosmobile.eplan.core.presentation.BaseApplication
import com.cosmobile.eplan.core.util.EPLAN_ENCRYPTED_PREFS_FILE
import com.cosmobile.eplan.core.util.MASTERKEY_TOKEN_SHARED_PREFS
import com.cosmobile.eplan.core.util.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(context: Application): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MASTERKEY_TOKEN_SHARED_PREFS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context.applicationContext,
            EPLAN_ENCRYPTED_PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}