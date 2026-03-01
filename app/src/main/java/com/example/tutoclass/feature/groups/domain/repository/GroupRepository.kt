package com.example.tutoclass.feature.groups.domain.repository

import com.example.tutoclass.feature.groups.domain.model.Group

interface GroupRepository {
    suspend fun createGroup(
        name: String,
        subject: String,
        description: String?,
        teacherName: String,
        date: String,
        accessCode: String
    ): Result<Group>

    suspend fun joinGroup(accessCode: String): Result<Group>

    suspend fun getGroupsByTeacher(teacherId: Int): Result<List<Group>>

    suspend fun getGroupsByStudent(studentId: Int): Result<List<Group>>
}
