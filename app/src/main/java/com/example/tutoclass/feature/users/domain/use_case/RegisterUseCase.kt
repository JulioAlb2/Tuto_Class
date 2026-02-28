package com.example.tutoclass.feature.users.domain.use_case

import com.example.tutoclass.feature.users.domain.model.User
import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        nombre: String,
        email: String,
        password: String,
        rol: String,
        materias: List<String>? = null
    ): Result<User> {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || rol.isBlank()) {
            return Result.failure(Exception("Todos los campos son obligatorios"))
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Formato de correo inválido"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        if (rol == "Maestro" && (materias == null || materias.isEmpty())) {
            return Result.failure(Exception("Debe ingresar al menos una materia"))
        }

        return repository.register(nombre, email, password, rol, materias)
    }
}