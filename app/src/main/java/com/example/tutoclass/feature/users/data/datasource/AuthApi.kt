package com.example.tutoclass.feature.users.data.datasource

import com.example.tutoclass.feature.users.data.remote.dto.LoginRequest
import com.example.tutoclass.feature.users.data.remote.dto.RegisterRequest
import com.example.tutoclass.feature.users.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface  AuthApi  {
    @POST ("auth/register/alumno")
    suspend fun  register(@Body request: RegisterRequest ) : UserResponse
    @POST ("auth/login")
    suspend fun  login (@Body request: LoginRequest) : UserResponse
    @POST ("auth/logout")
    suspend fun  logout()
}


