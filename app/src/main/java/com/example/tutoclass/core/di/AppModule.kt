package com.example.tutoclass.core.di

import com.example.tutoclass.feature.users.data.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.data.local.AuthLocalDataSourceImpl
import com.example.tutoclass.feature.users.data.remote.AuthApi
import com.example.tutoclass.feature.users.data.repository.AuthRepositoryImpl
import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://tutoapi.example.com/") // URL base de ejemplo
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}