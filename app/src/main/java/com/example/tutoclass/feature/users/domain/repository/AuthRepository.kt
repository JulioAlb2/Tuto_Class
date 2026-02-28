package com.example.tutoclass.feature.users.domain.repository

import com.example.tutoclass.feature.users.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<User?>
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun register(nombre: String, email: String, pass: String, rol: String): Result<User>
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}