package com.example.tutoclass.feature.groups.data.repository

import android.util.Log
import com.example.tutoclass.core.di.NetworkModule
import com.example.tutoclass.feature.groups.data.datasource.remote.MessageApi
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.CreateMessageRequest
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.MessageResponse
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.UpdateMessageRequest
import com.example.tutoclass.feature.groups.domain.model.Message
import com.example.tutoclass.feature.groups.domain.repository.MessageEvent
import com.example.tutoclass.feature.groups.domain.repository.MessageRepository
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import retrofit2.Retrofit
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: MessageApi,
    @NetworkModule.SseOkHttpClient private val sseClient: OkHttpClient,
    private val gson: Gson,
    private val retrofit: Retrofit,
    private val authLocalDataSource: AuthLocalDataSource
) : MessageRepository {

    private val baseUrl: String by lazy {
        val url = retrofit.baseUrl().toString()
        if (url.endsWith("/")) url else "$url/"
    }

    override suspend fun getGroupMessages(groupId: Int): Result<List<Message>> {
        return try {
            val response = api.getGroupMessages(groupId)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("SSE_REPO", "Error en getGroupMessages: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun createMessage(groupId: Int, userName: String, text: String): Result<Message> {
        return try {
            val response = api.createMessage(CreateMessageRequest(groupId, userName, text, "texto"))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMessage(messageId: Int, text: String): Result<Message> {
        return try {
            val response = api.updateMessage(messageId, UpdateMessageRequest(text))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> {
        return try {
            api.deleteMessage(messageId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun listenToGroupEvents(groupId: Int): Flow<MessageEvent> = callbackFlow {
        var currentEventSource: EventSource? = null
        var isClosedManually = false
        
        launch {
            try {
                val user = authLocalDataSource.getUser().first()
                val token = user?.token
                val url = "${baseUrl}groups/$groupId/events"

                fun connect() {
                    if (isClosedManually) return
                    
                    val request = Request.Builder()
                        .url(url)
                        .header("Accept", "text/event-stream")
                        .header("Cache-Control", "no-cache, no-store, must-revalidate")
                        .header("Pragma", "no-cache")
                        .header("X-Accel-Buffering", "no")
                        .apply {
                            if (!token.isNullOrEmpty()) {
                                header("Authorization", "Bearer $token")
                            }
                        }
                        .build()

                    val listener = object : EventSourceListener() {
                        override fun onOpen(eventSource: EventSource, response: Response) {
                            Log.d("SSE_REPO", "SSE Conectado - Modo instantáneo")
                        }

                        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                            Log.d("SSE_REPO", "Evento: $type, Data: $data")
                            try {
                                // Tu API envía el objeto del mensaje en 'data'
                                val directMessage = try { gson.fromJson(data, MessageResponse::class.java) } catch (e: Exception) { null }
                                
                                if (directMessage != null && directMessage.id != 0) {
                                    val event = when (type) {
                                        "message_created" -> MessageEvent.Created(directMessage.toDomain())
                                        "message_updated" -> MessageEvent.Updated(directMessage.toDomain())
                                        "message_deleted" -> MessageEvent.Deleted(directMessage.id)
                                        else -> MessageEvent.Created(directMessage.toDomain())
                                    }
                                    trySend(event)
                                }
                            } catch (e: Exception) {
                                Log.e("SSE_REPO", "Error parseo: ${e.message}")
                            }
                        }

                        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                            if (!isClosedManually) {
                                Log.w("SSE_REPO", "Fallo SSE, reintentando en 1s...")
                                launch { delay(1000); connect() }
                            }
                        }
                        
                        override fun onClosed(eventSource: EventSource) {
                            if (!isClosedManually) connect()
                        }
                    }

                    currentEventSource = EventSources.createFactory(sseClient).newEventSource(request, listener)
                }

                connect()

            } catch (e: Exception) {
                Log.e("SSE_REPO", "Error: ${e.message}")
            }
        }

        awaitClose {
            isClosedManually = true
            currentEventSource?.cancel()
        }
    }
}

data class SseMessageWrapper(val type: String?, val data: MessageResponse?)

fun MessageResponse.toDomain(): Message = Message(
    id = id,
    groupId = groupId,
    userId = userId,
    userName = userName,
    userRole = userRole,
    text = text,
    type = type,
    createdAt = createdAt,
    edited = when (edited) {
        is Boolean -> edited
        is Number -> edited.toInt() == 1
        else -> false
    },
    editedAt = editedAt
)
