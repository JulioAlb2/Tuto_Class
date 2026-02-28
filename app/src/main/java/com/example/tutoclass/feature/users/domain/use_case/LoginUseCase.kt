package com.example.tutoclass.feature.users.domain.use_case

import com.example.tutoclass.feature.users.domain.model.User
import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<User> {
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(Exception("El correo y la contraseña son obligatorios"))
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Formato de correo inválido"))
        }

        return repository.login(email, pass)
    }
}