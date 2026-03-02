package com.example.tutoclass.feature.groups.domain.use_case

import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.repository.GroupRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(accessCode: String): Result<Group> {
        if (accessCode.isBlank()) {
            return Result.failure(Exception("El código de acceso no puede estar vacío"))
        }
        return repository.joinGroup(accessCode)
    }
}
