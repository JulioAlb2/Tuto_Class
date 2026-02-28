package com.example.tutoclass.feature.users.data.datasource

import com.example.tutoclass.feature.users.data.datasource.remote.dto.LoginRequest
import com.example.tutoclass.feature.users.data.datasource.remote.dto.RegisterRequest
import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): UserResponse

    @POST("auth/register/alumno")
    suspend fun registerAlumno(@Body request: RegisterRequest): UserResponse

    @POST("auth/register/maestro")
    suspend fun registerMaestro(@Body request: RegisterRequest): UserResponse

    @POST("auth/logout")
    suspend fun logout()
}