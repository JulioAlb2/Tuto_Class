package com.example.tutoclass.feature.groups.domain.use_case

import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.repository.GroupRepository
import javax.inject.Inject

class GetStudentGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(studentId: Int): Result<List<Group>> {
        return repository.getGroupsByStudent(studentId)
    }
}
