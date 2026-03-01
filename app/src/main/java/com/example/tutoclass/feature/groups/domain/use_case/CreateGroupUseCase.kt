package com.example.tutoclass.feature.groups.domain.use_case

import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(
        name: String,
        subject: String,
        description: String?,
        teacherName: String,
        date: String,
        accessCode: String
    ): Result<Group> {
        if (name.isBlank() || subject.isBlank() || accessCode.isBlank()) {
            return Result.failure(Exception("Nombre, materia y código son obligatorios"))
        }
        return repository.createGroup(name, subject, description, teacherName, date, accessCode)
    }
}
