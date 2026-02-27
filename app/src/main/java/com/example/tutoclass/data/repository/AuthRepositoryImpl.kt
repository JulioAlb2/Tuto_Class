package com.example.tutoclass.data.repository

import com.example.tutoclass.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun login(email: String, pass: String): Result<Boolean> {
        return Result.success(true) // Simulación de login exitoso
    }
}