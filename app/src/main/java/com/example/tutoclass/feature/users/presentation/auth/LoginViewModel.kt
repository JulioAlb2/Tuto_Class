package com.example.tutoclass.feature.users.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.users.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    fun onLoginChanged(email: String, pass: String) {
        _uiState.update { it.copy(email = email, password = pass, error = null) }
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = loginUseCase(
                email = _uiState.value.email,
                pass = _uiState.value.password
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