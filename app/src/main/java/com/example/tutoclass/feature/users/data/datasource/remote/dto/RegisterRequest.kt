package com.example.tutoclass.feature.users.data.datasource.remote.dto
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val nombre: String,
    val email: String,
    val password: String,
    @SerializedName("role")
    val role: String,
    val materias: List<String>? = null
)