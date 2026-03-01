package com.example.tutoclass.core.network

import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localDataSource: AuthLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlPath = request.url().encodedPath()
        
        if (urlPath.contains("auth/login") ||
            urlPath.contains("auth/register")) {
            return chain.proceed(request)
        }

        val user = runBlocking { localDataSource.getUser().first() }
        val token = user?.token

        val newRequest = request.newBuilder()
        if (!token.isNullOrEmpty()) {
            newRequest.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(newRequest.build())
    }
}
