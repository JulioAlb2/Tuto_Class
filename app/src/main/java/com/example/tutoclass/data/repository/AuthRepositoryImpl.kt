package com.example.tutoclass.data.repository

import com.example.tutoclass.data.remote.TutoApi
import com.example.tutoclass.data.remote.dto.toDomain
import com.example.tutoclass.domain.model.Group
import com.example.tutoclass.domain.repository.StudentRepository
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val api: TutoApi
) : StudentRepository {

    override suspend fun getGroups(): List<Group> {
        // API aqui julio xd
        return api.getGroups().map { it.toDomain() }
    }

    override suspend fun joinGroup(codigoAcceso: String): Result<Unit> {
        return Result.success(Unit)
    }
}