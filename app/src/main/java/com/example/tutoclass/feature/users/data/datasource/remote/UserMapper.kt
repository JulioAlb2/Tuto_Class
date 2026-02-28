package com.example.tutoclass.feature.users.data.datasource.remote

import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserResponse
import com.example.tutoclass.feature.users.domain.model.User


fun UserResponse?.toDomain(): User {
    if (this == null) {
        return User(
            id = "",
            nombre = "Usuario",
            email = "",
            rol = "",
            token = null
        )
    }

    return User(
        id = this.id ?: "",
        nombre = this.nombre ?: "Usuario",
        email = this.email ?: "",
        rol = this.rol ?: "",
        token = this.token
    )
}


fun User.toDto(): UserResponse {
    return UserResponse(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        rol = this.rol,
        token = this.token
    )
}