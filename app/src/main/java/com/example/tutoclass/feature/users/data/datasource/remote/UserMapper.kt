package com.example.tutoclass.feature.users.data.datasource.remote

import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserDto
import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserResponse
import com.example.tutoclass.feature.users.domain.model.User


fun UserResponse?.toDomain(): User {
    val userDto = this?.user
    
    if (this == null || userDto == null) {
        return User(
            id = "",
            nombre = "Usuario",
            email = "",
            rol = "",
            token = this?.token
        )
    }

    return User(
        id = userDto.id?.toString() ?: "",
        nombre = userDto.nombre ?: "Usuario",
        email = userDto.email ?: "",
        rol = userDto.rol ?: "",
        token = this.token
    )
}


fun User.toDto(): UserResponse {
    return UserResponse(
        user = UserDto(
            id = try { this.id.toInt() } catch (e: Exception) { null },
            nombre = this.nombre,
            email = this.email,
            rol = this.rol
        ),
        token = this.token
    )
}