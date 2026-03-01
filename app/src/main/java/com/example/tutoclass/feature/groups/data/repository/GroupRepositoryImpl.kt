package com.example.tutoclass.feature.groups.data.repository

import com.example.tutoclass.feature.groups.data.datasource.remote.GroupApi
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.CreateGroupRequest
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.JoinGroupRequest
import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val api: GroupApi
) : GroupRepository {

    override suspend fun createGroup(
        name: String,
        subject: String,
        description: String?,
        teacherName: String,
        date: String,
        accessCode: String
    ): Result<Group> {
        return try {
            val response = api.createGroup(
                CreateGroupRequest(
                    name = name,
                    subject = subject,
                    description = description,
                    teacherName = teacherName,
                    date = date,
                    accessCode = accessCode
                )
            )
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun joinGroup(accessCode: String): Result<Group> {
        return try {
            val response = api.joinGroup(JoinGroupRequest(accessCode))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupsByTeacher(teacherId: Int): Result<List<Group>> {
        return try {
            val response = api.getGroupsByTeacher(teacherId)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupsByStudent(studentId: Int): Result<List<Group>> {
        return try {
            val response = api.getGroupsByStudent(studentId)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

fun com.example.tutoclass.feature.groups.data.datasource.remote.dto.GroupResponse.toDomain(): Group {
    return Group(
        id = id,
        name = name,
        subject = subject,
        description = description,
        teacherId = teacherId,
        teacherName = teacherName,
        date = date,
        accessCode = accessCode,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
