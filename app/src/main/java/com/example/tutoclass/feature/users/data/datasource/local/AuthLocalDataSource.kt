package com.example.tutoclass.feature.users.data.datasource.local

import com.example.tutoclass.feature.users.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun saveUser(user: User)
    fun getUser(): Flow<User?>
    suspend fun clearUser()
}