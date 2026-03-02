package com.example.tutoclass.feature.groups.domain.model

data class GroupUser(
    val id: Int,
    val groupId: Int,
    val userId: Int,
    val joinedAt: String
)
