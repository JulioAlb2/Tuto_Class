package com.example.tutoclass.feature.users.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName
data class UserResponse(
    @SerializedName("user")
    val userData: UserData?,
    val token: String?
)

data class UserData(
    val id: Any?,
    val nombre: String?,
    val email: String?,
    val rol: String?
)