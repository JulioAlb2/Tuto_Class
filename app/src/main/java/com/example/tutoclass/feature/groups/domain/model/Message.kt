package com.example.tutoclass.feature.groups.domain.model

data class Message(
    val id: Int,
    val groupId: Int,
    val userId: Int,
    val userName: String,
    val userRole: String,
    val text: String,
    val type: String,
    val createdAt: String,
    val edited: Boolean,
    val editedAt: String?
)
