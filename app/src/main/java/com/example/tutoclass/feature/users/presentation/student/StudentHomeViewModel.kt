package com.example.tutoclass.feature.users.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.example.tutoclass.domain.model.Group
// import com.example.tutoclass.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Group(val nombre: String, val nombreProfesor: String, val materia: String)

data class StudentHomeState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // val groups = repository.getGroups()
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