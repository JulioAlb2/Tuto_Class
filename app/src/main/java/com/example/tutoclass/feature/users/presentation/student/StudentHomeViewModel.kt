package com.example.tutoclass.feature.users.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.use_case.GetStudentGroupsUseCase
import com.example.tutoclass.feature.groups.domain.use_case.JoinGroupUseCase
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentHomeState(
    val user: User? = null,
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val joinSuccess: Boolean = false
)

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val getStudentGroupsUseCase: GetStudentGroupsUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(StudentHomeState())
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val user = authLocalDataSource.getUser().first()
                _state.update { it.copy(user = user) }
                
                val userId = user?.id?.toIntOrNull()
                if (userId != null) {
                    val result = getStudentGroupsUseCase(userId)
                    result.onSuccess { groups ->
                        _state.update { it.copy(groups = groups, isLoading = false) }
                    }.onFailure { e ->
                        _state.update { it.copy(error = e.message, isLoading = false) }
                    }
                } else {
                    _state.update { it.copy(error = "Sesión no válida", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val user = _state.value.user ?: authLocalDataSource.getUser().first()
            val userId = user?.id?.toIntOrNull()

            if (userId != null) {
                val result = getStudentGroupsUseCase(userId)
                result.onSuccess { groups ->
                    _state.update { it.copy(groups = groups) }
                }
            }
        }
    }

    fun joinGroup(accessCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, joinSuccess = false, error = null) }
            val result = joinGroupUseCase(accessCode)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, joinSuccess = true) }
                loadGroups()
            }.onFailure { e ->
                _state.update { it.copy(error = e.message ?: "Error al unirse al grupo", isLoading = false) }
            }
        }
    }

    fun resetJoinState() {
        _state.update { it.copy(joinSuccess = false, error = null) }
    }
}
