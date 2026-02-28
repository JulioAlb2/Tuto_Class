package com.example.tutoclass.feature.users.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.users.domain.model.User
import com.example.tutoclass.feature.users.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Group(val nombre: String, val nombreProfesor: String, val materia: String)

data class StudentHomeState(
    val user: User? = null,
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    init {
        loadUserData()
        loadGroups()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            android.util.Log.d("DEBUG_USER", "Usuario cargado: $currentUser")
            _state.update { it.copy(user = currentUser) }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // TODO: Usar un repositorio real para grupos cuando esté disponible
                val groups = listOf(
                    Group("Moviles 1", "Ali López Zúnun", "Programación"),
                    Group("Inglés 3", "Sarah Smith", "Idiomas")
                )
                _state.update { it.copy(groups = groups, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}