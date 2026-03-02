package com.example.tutoclass.core.di

import com.example.tutoclass.core.network.AuthInterceptor
import com.example.tutoclass.feature.groups.data.datasource.remote.GroupApi
import com.example.tutoclass.feature.groups.data.datasource.remote.MessageApi
import com.example.tutoclass.feature.users.data.datasource.AuthApi
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class SseOkHttpClient

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAuthInterceptor(authLocalDataSource: AuthLocalDataSource): AuthInterceptor {
        return AuthInterceptor(authLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @SseOkHttpClient
    @Provides
    @Singleton
    fun provideSseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .protocols(listOf(Protocol.HTTP_1_1)) // Forzar HTTP/1.1 para evitar problemas de streaming
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://practicasoftware.fun/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupApi(retrofit: Retrofit): GroupApi {
        return retrofit.create(GroupApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageApi(retrofit: Retrofit): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }
}
