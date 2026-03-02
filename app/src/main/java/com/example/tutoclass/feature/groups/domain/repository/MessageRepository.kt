package com.example.tutoclass.feature.groups.domain.repository

import com.example.tutoclass.feature.groups.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getGroupMessages(groupId: Int): Result<List<Message>>
    suspend fun createMessage(groupId: Int, userName: String, text: String): Result<Message>
    suspend fun updateMessage(messageId: Int, text: String): Result<Message>
    suspend fun deleteMessage(messageId: Int): Result<Unit>
    fun listenToGroupEvents(groupId: Int): Flow<MessageEvent>
}

sealed class MessageEvent {
    data class Created(val message: Message) : MessageEvent()
    data class Updated(val message: Message) : MessageEvent()
    data class Deleted(val messageId: Int) : MessageEvent()
    data object Error : MessageEvent()
}
