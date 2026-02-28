package com.example.tutoclass.feature.users.data.repository

import android.util.Log
import com.example.tutoclass.feature.users.data.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.data.remote.AuthApi
import com.example.tutoclass.feature.users.data.remote.dto.LoginRequest
import com.example.tutoclass.feature.users.data.remote.dto.RegisterRequest
import com.example.tutoclass.feature.users.data.remote.dto.toDomain
import com.example.tutoclass.feature.users.domain.model.User
import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override val authState: Flow<User?> = localDataSource.getUser()

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, pass))
            val user = response.toDomain()
            
            if (user.token.isNullOrEmpty()) {
                Log.e("AWS_AUTH", "Error: La API de login no devolvió un token válido")
                return Result.failure(Exception("Token no recibido del servidor"))
            }

            Log.d("AWS_AUTH", "Login exitoso. Guardando token para el usuario: ${user.id}")
            localDataSource.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Log.e("AWS_AUTH", "Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, pass: String, rol: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(nombre, email, pass, rol))
            val user = response.toDomain()
            
            if (!user.token.isNullOrEmpty()) {
                localDataSource.saveUser(user)
            }
            Result.success(user)
        } catch (e: Exception) {
            Log.e("AWS_AUTH", "Error en registro: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        localDataSource.clearUser()
    }

    override suspend fun getCurrentUser(): User? {
        return localDataSource.getUser().firstOrNull()
    }
}