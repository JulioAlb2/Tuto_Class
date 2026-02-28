package com.example.tutoclass.feature.users.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.users.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "Estudiante",
    val subjects: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) { _uiState.update { it.copy(name = name, error = null) } }
    fun onEmailChanged(email: String) { _uiState.update { it.copy(email = email, error = null) } }
    fun onPasswordChanged(pass: String) { _uiState.update { it.copy(password = pass, error = null) } }
    fun onRoleChanged(role: String) { _uiState.update { it.copy(role = role, error = null) } }
    fun onSubjectsChanged(subjects: String) { _uiState.update { it.copy(subjects = subjects, error = null) } }

    fun signUp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val subjectsList = if (_uiState.value.role == "Maestro") {
                _uiState.value.subjects.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            } else null

            val result = registerUseCase(
                nombre = _uiState.value.name,
                email = _uiState.value.email,
                password = _uiState.value.password,
                rol = _uiState.value.role,
                materias = subjectsList
            )

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}