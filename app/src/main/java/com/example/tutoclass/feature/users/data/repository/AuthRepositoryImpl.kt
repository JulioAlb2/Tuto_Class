package com.example.tutoclass.feature.users.data.repository

import android.util.Log
import com.example.tutoclass.feature.users.data.datasource.AuthApi
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.data.datasource.remote.dto.LoginRequest
import com.example.tutoclass.feature.users.data.datasource.remote.dto.RegisterRequest
import com.example.tutoclass.feature.users.data.datasource.remote.toDomain
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
            Log.d("AUTH_REPO", "Intentando login para: $email")
            val response = api.login(LoginRequest(email, pass))
            
            // Log para ver la respuesta cruda (opcional, ayuda a debuguear)
            Log.d("AUTH_REPO", "Respuesta recibida: $response")
            
            val user = response.toDomain()
            
            // DIAGNÓSTICO CRÍTICO
            Log.e("PRUEBA_DIAGNOSTICO", "DATOS MAPEADOS -> ID: ${user.id}, NOMBRE: ${user.nombre}, ROL: '${user.rol}'")

            if (user.token.isNullOrEmpty()) {
                return Result.failure(Exception("Token no recibido del servidor"))
            }

            if (user.rol.isEmpty()) {
                Log.e("AUTH_REPO", "¡ATENCIÓN! El rol llegó VACÍO tras el mapeo. Revisa la estructura del JSON.")
            }

            localDataSource.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun register(
        nombre: String,
        email: String,
        pass: String,
        rol: String,
        materias: List<String>?
    ): Result<User> {
        return try {
            val request = RegisterRequest(
                nombre = nombre,
                email = email,
                password = pass,
                role = rol,
                materias = materias
            )

            val response = if (rol.contains("Maestro", ignoreCase = true)) {
                api.registerMaestro(request)
            } else {
                api.registerAlumno(request)
            }

            val user = response.toDomain()
            Log.e("PRUEBA_DIAGNOSTICO", "REGISTRO -> ROL MAPEADO: '${user.rol}'")

            if (!user.token.isNullOrEmpty()) {
                localDataSource.saveUser(user)
            }
            Result.success(user)
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "Error en registro: ${e.message}")
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
