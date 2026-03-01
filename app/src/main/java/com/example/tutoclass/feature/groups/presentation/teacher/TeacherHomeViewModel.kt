package com.example.tutoclass.feature.groups.presentation.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.use_case.CreateGroupUseCase
import com.example.tutoclass.feature.groups.domain.use_case.GetTeacherGroupsUseCase
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class TeacherHomeState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastCreatedGroup: Group? = null,
    val teacherName: String = ""
)

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val getTeacherGroupsUseCase: GetTeacherGroupsUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(TeacherHomeState())
    val state = _state.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val user = authLocalDataSource.getUser().first()
            val userId = user?.id?.toIntOrNull()

            if (user != null) {
                _state.update { it.copy(teacherName = user.nombre) }
            }

            if (userId != null) {
                val result = getTeacherGroupsUseCase(userId)
                result.onSuccess { groups ->
                    _state.update { it.copy(groups = groups, isLoading = false, error = null) }
                }.onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
            } else {
                _state.update { it.copy(error = "Sesión inválida", isLoading = false) }
            }
        }
    }

    fun createGroup(name: String, subject: String, description: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, lastCreatedGroup = null) }
            val user = authLocalDataSource.getUser().first()
            val userId = user?.id?.toIntOrNull()

            if (userId != null && user != null) {
                val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
                val generatedCode = UUID.randomUUID().toString().substring(0, 6).uppercase()

                val result = createGroupUseCase(
                    name = name,
                    subject = subject,
                    description = description,
                    teacherName = user.nombre,
                    date = currentDate,
                    accessCode = generatedCode
                )
                result.onSuccess { newGroup ->
                    _state.update { it.copy(isLoading = false, lastCreatedGroup = newGroup) }
                    loadGroups()
                }.onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
            } else {
                _state.update { it.copy(error = "No se pudo identificar al maestro", isLoading = false) }
            }
        }
    }

    fun clearLastCreatedGroup() {
        _state.update { it.copy(lastCreatedGroup = null) }
    }
}
