package com.example.tutoclass.feature.groups.data.datasource.remote.dto

data class CreateGroupRequest(
    val name: String,
    val subject: String,
    val description: String?,
    val teacherName: String,
    val date: String,
    val accessCode: String,
    val status: String = "activa"
)

data class JoinGroupRequest(
    val accessCode: String
)

data class UpdateGroupRequest(
    val name: String?,
    val subject: String?,
    val description: String?,
    val date: String?,
    val status: String?
)
