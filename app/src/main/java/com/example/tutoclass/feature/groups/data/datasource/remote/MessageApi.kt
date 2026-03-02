package com.example.tutoclass.feature.groups.data.datasource.remote

import com.example.tutoclass.feature.groups.data.datasource.remote.dto.CreateMessageRequest
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.MessageResponse
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.UpdateMessageRequest
import retrofit2.http.*

interface MessageApi {
    @GET("groups/{groupId}/messages")
    suspend fun getGroupMessages(@Path("groupId") groupId: Int): List<MessageResponse>

    @POST("messages")
    suspend fun createMessage(@Body request: CreateMessageRequest): MessageResponse

    @GET("messages/{id}")
    suspend fun getMessageById(@Path("id") id: Int): MessageResponse

    @PATCH("messages/{id}")
    suspend fun updateMessage(@Path("id") id: Int, @Body request: UpdateMessageRequest): MessageResponse

    @DELETE("messages/{id}")
    suspend fun deleteMessage(@Path("id") id: Int)
}
