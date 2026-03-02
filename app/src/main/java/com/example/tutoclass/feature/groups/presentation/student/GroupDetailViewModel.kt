package com.example.tutoclass.feature.groups.presentation.student

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutoclass.feature.groups.domain.model.Group
import com.example.tutoclass.feature.groups.domain.model.Message
import com.example.tutoclass.feature.groups.domain.repository.GroupRepository
import com.example.tutoclass.feature.groups.domain.repository.MessageEvent
import com.example.tutoclass.feature.groups.domain.repository.MessageRepository
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupDetailState(
    val group: Group? = null,
    val students: List<User> = emptyList(),
    val messages: List<Message> = emptyList(),
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val messageRepository: MessageRepository,
    private val authLocalDataSource: AuthLocalDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(GroupDetailState())
    val state = _state.asStateFlow()

    private val groupId: Int? = savedStateHandle.get<String>("groupId")?.toIntOrNull()

    init {
        if (groupId != null) {
            loadInitialData(groupId)
            observeMessages(groupId)
        } else {
            _state.update { it.copy(error = "ID de grupo inválido") }
        }
    }

    private fun loadInitialData(groupId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val user = authLocalDataSource.getUser().first()
            _state.update { it.copy(currentUser = user) }

            groupRepository.getGroupById(groupId)
                .onSuccess { group -> _state.update { it.copy(group = group) } }
                .onFailure { e -> _state.update { it.copy(error = e.message) } }

            groupRepository.getGroupStudents(groupId)
                .onSuccess { students -> _state.update { it.copy(students = students) } }

            messageRepository.getGroupMessages(groupId)
                .onSuccess { messages ->
                    _state.update { it.copy(messages = messages.sortedBy { it.createdAt }) }
                }
                .onFailure { e ->
                    Log.e("GroupDetail", "Error cargando historial", e)
                    _state.update { it.copy(error = "No se pudieron cargar los mensajes") }
                }
            
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun observeMessages(groupId: Int) {
        messageRepository.listenToGroupEvents(groupId)
            .onEach { event ->
                Log.d("GroupDetailVM", "Evento SSE: $event")
                when (event) {
                    is MessageEvent.Created -> {
                        _state.update { currentState ->
                            val updatedMessages = (currentState.messages.filter { it.id != event.message.id } + event.message)
                                .sortedBy { it.createdAt }
                            currentState.copy(messages = updatedMessages)
                        }
                    }
                    is MessageEvent.Updated -> {
                        _state.update { currentState ->
                            val updatedMessages = currentState.messages.map { 
                                if (it.id == event.message.id) event.message else it 
                            }
                            currentState.copy(messages = updatedMessages)
                        }
                    }
                    is MessageEvent.Deleted -> {
                        _state.update { currentState ->
                            currentState.copy(messages = currentState.messages.filter { it.id != event.messageId })
                        }
                    }
                    MessageEvent.Error -> {
                        Log.e("GroupDetailVM", "Error en flujo SSE")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || groupId == null) return
        
        viewModelScope.launch {
            val user = _state.value.currentUser ?: return@launch
            _state.update { it.copy(isSending = true) }
            
            messageRepository.createMessage(groupId, user.nombre, text)
                .onFailure { e ->
                    _state.update { it.copy(error = "Error al enviar: ${e.message}") }
                }
            
            _state.update { it.copy(isSending = false) }
        }
    }

    fun updateMessage(messageId: Int, newText: String) {
        if (newText.isBlank()) return
        
        viewModelScope.launch {
            messageRepository.updateMessage(messageId, newText)
                .onFailure { e ->
                    _state.update { it.copy(error = "Error al editar: ${e.message}") }
                }
        }
    }

    fun deleteMessage(messageId: Int) {
        viewModelScope.launch {
            messageRepository.deleteMessage(messageId)
                .onFailure { e ->
                    _state.update { it.copy(error = "Error al eliminar: ${e.message}") }
                }
        }
    }
}
