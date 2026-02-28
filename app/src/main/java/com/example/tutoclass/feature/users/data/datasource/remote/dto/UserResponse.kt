package com.example.tutoclass.feature.users.data.datasource.remote.dto

data class UserResponse(
    val user: UserDto?,
    val token: String?
)

data class UserDto(
    val id: Int?,
    val nombre: String?,
    val email: String?,
    val rol: String?
)