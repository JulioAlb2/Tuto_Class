package com.example.tutoclass.feature.users.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    @SerializedName("password")
    val pass: String
)