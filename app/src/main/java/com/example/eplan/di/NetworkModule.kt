package com.example.eplan.di

import com.example.eplan.cache.UserDao
import com.example.eplan.network.LoginService
import com.example.eplan.network.WorkActivityService
import com.example.eplan.network.model.UserDtoMapper
import com.example.eplan.network.model.UserService
import com.example.eplan.network.model.WorkActivityDtoMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TOKEN_HEADER = "Bearer "
    private var userToken = ""

    fun setToken(token: String) {
        userToken = token
    }

    fun getToken(header: Boolean = true): String {
        return if (header) {
            TOKEN_HEADER + userToken
        } else {
            userToken
        }
    }


    @Singleton
    @Provides
    fun provideWorkActivityDtoMapper(): WorkActivityDtoMapper {
        return WorkActivityDtoMapper()
    }

    @Singleton
    @Provides
    fun provideWorkActivityService(): WorkActivityService {
        return Retrofit.Builder()
            .baseUrl("https://gest.eplanweb.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WorkActivityService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl("https://gest.eplanweb.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserDtoMapper(): UserDtoMapper {
        return UserDtoMapper()
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService {
        return Retrofit.Builder()
            .baseUrl("https://ciao.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(UserService::class.java)
    }
}