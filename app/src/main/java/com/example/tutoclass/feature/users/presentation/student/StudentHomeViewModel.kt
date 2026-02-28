package com.example.tutoclass.feature.users.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.domain.model.Group
import com.example.tutoclass.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentHomeState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val repository: StudentRepository
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
                val groups = repository.getGroups()
                _state.update { it.copy(groups = groups, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}