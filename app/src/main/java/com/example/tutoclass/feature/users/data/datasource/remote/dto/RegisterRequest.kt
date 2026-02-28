package com.example.tutoclass.feature.users.data.datasource.remote.dto

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val materias: List<String>? = null
)