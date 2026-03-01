package com.example.tutoclass.feature.users.data.datasource.remote

import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserResponse
import com.example.tutoclass.feature.users.domain.model.User

fun UserResponse?.toDomain(): User {
    val userData = this?.userData
    
    val rawId = userData?.id?.toString() ?: ""
    val finalId = if (rawId.endsWith(".0")) rawId.substringBefore(".0") else rawId
    
    val finalName = userData?.nombre ?: "Usuario"
    val finalRole = userData?.rol ?: ""
    val finalEmail = userData?.email ?: ""

    return User(
        id = finalId,
        nombre = finalName,
        email = finalEmail,
        rol = finalRole,
        token = this?.token
    )
}

fun User.toDto(): UserResponse {
    return UserResponse(
        userData = null,
        token = this.token
    )
}