package com.example.tutoclass.feature.groups.data.datasource.remote

import com.example.tutoclass.feature.groups.data.datasource.remote.dto.CreateGroupRequest
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.GroupResponse
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.JoinGroupRequest
import com.example.tutoclass.feature.groups.data.datasource.remote.dto.UpdateGroupRequest
import com.example.tutoclass.feature.users.data.datasource.remote.dto.UserData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupApi {
    @GET("groups")
    suspend fun getActiveGroups(): List<GroupResponse>

    @GET("groups/{id}")
    suspend fun getGroupById(@Path("id") id: Int): GroupResponse

    @GET("groups/{id}/students")
    suspend fun getGroupStudents(@Path("id") id: Int): List<UserData>

    @POST("groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): GroupResponse

    @GET("groups/teacher/{teacherId}")
    suspend fun getGroupsByTeacher(@Path("teacherId") teacherId: Int): List<GroupResponse>

    @GET("groups/student/{studentId}")
    suspend fun getGroupsByStudent(@Path("studentId") studentId: Int): List<GroupResponse>

    @POST("groups/join")
    suspend fun joinGroup(@Body request: JoinGroupRequest): GroupResponse

    @PATCH("groups/{id}")
    suspend fun updateGroup(@Path("id") id: Int, @Body request: UpdateGroupRequest): GroupResponse

    @DELETE("groups/{id}")
    suspend fun deleteGroup(@Path("id") id: Int)

    @POST("groups/{id}/leave")
    suspend fun leaveGroup(@Path("id") id: Int)

    @GET("groups/{id}/enrolled")
    suspend fun isEnrolled(@Path("id") id: Int): Boolean
}
