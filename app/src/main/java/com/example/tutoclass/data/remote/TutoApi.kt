package com.example.tutoclass.data.remote

import com.example.tutoclass.data.remote.dto.GroupDto
import retrofit2.http.GET

interface TutoApi {
    // Esta ruta debe existir en el API que maneja el MySQL
    @GET("grupos")
    suspend fun getGroups(): List<GroupDto>
}