package com.example.tutoclass.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tutoclass.domain.repository.AuthRepository

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) { _uiState.update { it.copy(name = name) } }
    fun onEmailChanged(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChanged(pass: String) { _uiState.update { it.copy(password = pass) } }

    fun signUp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Aquí llamarías a repository.signUp si ya lo creaste
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
