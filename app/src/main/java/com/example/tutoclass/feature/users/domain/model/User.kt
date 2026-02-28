package com.example.tutoclass.feature.users.domain.model

data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: String,
    val token: String? = null
)