package com.example.tutoclass.feature.groups.data.datasource.remote.dto

data class MessageResponse(
    val id: Int,
    val groupId: Int,
    val userId: Int,
    val userName: String,
    val userRole: String,
    val text: String,
    val type: String,
    val createdAt: String,
    val edited: Any?, // Flexible: puede ser Int (0/1) o Boolean
    val editedAt: String?
)

data class CreateMessageRequest(
    val groupId: Int,
    val userName: String,
    val text: String,
    val type: String
)

data class UpdateMessageRequest(
    val text: String
)
