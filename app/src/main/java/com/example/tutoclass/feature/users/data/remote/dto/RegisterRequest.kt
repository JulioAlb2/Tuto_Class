package com.example.tutoclass.feature.users.data.remote.dto

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val pass: String,
    val rol: String
)