package com.example.tutoclass.feature.groups.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class GroupResponse(
    val id: Int,
    val name: String,
    val subject: String,
    val description: String?,
    val teacherId: Int,
    val teacherName: String,
    val date: String,
    val accessCode: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
