package com.example.tutoclass.presentation.auth

data class LoginState(
    val email: String = "",
    val password: String = "", // <--- Verifica que se llame EXACTAMENTE así
    val isLoading: Boolean = false,
    val error: String? = null
)