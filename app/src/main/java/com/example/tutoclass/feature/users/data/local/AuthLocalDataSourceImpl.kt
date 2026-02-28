package com.example.tutoclass.feature.users.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tutoclass.feature.users.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthLocalDataSource {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val NOMBRE = stringPreferencesKey("nombre")
        private val EMAIL = stringPreferencesKey("email")
        private val ROL = stringPreferencesKey("rol")
        private val TOKEN = stringPreferencesKey("token")
    }

    override suspend fun saveUser(user: User) {
        try {
            context.dataStore.edit { preferences ->
                preferences[USER_ID] = user.id
                preferences[NOMBRE] = user.nombre
                preferences[EMAIL] = user.email
                preferences[ROL] = user.rol
                user.token?.let {
                    preferences[TOKEN] = it
                    Log.d("AUTH_LOCAL", "Token guardado correctamente: ${it.take(10)}...")
                }
            }
        } catch (e: Exception) {
            Log.e("AUTH_LOCAL", "Error al guardar usuario en DataStore: ${e.message}")
        }
    }

    override fun getUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[USER_ID]
            val token = preferences[TOKEN]

            if (id == null || token == null) {
                Log.w("AUTH_LOCAL", "getUser: No se encontró sesión (id=$id, token=${token != null})")
                return@map null
            }

            User(
                id = id,
                nombre = preferences[NOMBRE] ?: "Usuario",
                email = preferences[EMAIL] ?: "",
                rol = preferences[ROL] ?: "",
                token = token
            )
        }
    }

    override suspend fun clearUser() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            Log.d("AUTH_LOCAL", "Sesión eliminada de DataStore")
        } catch (e: Exception) {
            Log.e("AUTH_LOCAL", "Error al limpiar DataStore: ${e.message}")
        }
    }
}