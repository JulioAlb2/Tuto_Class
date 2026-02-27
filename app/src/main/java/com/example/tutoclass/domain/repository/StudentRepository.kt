package com.example.tutoclass.domain.repository

import com.example.tutoclass.domain.model.Group

interface StudentRepository {
    // Obtener grupos disponibles (Select de la tabla 'grupos')
    suspend fun getGroups(): List<Group>

    // Unirse a un grupo (Insert en la tabla 'grupo_usuarios')
    suspend fun joinGroup(codigoAcceso: String): Result<Unit>
}