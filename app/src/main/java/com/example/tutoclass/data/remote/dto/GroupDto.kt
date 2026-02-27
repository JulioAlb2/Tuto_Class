package com.example.tutoclass.data.remote.dto

import com.example.tutoclass.domain.model.Group

data class GroupDto(
    val id: Int,
    val nombre: String,
    val materia: String,
    val descripcion: String?,
    val profesor_id: Int,
    val nombre_profesor: String,
    val fecha: String,
    val codigo_acceso: String,
    val estado: String
)

fun GroupDto.toDomain(): Group {
    return Group(
        id = id,
        nombre = nombre,
        materia = materia,
        descripcion = descripcion,
        profesorId = profesor_id,
        nombreProfesor = nombre_profesor,
        fecha = fecha,
        codigoAcceso = codigo_acceso,
        estado = estado
    )
}