package com.example.tutoclass.domain.model

data class User(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String // Refleja la columna 'rol' del SQL
)