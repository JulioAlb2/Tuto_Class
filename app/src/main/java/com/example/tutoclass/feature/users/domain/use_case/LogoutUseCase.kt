package com.example.tutoclass.feature.users.domain.use_case

import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}