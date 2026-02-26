package com.example.tutoclass.domain.repository

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<Boolean>
}