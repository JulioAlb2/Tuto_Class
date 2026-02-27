package com.example.tutoclass.domain.model

data class Group(
    val id: Int,
    val nombre: String,
    val materia: String,
    val descripcion: String?,
    val profesorId: Int, // Columna 'profesor_id'
    val nombreProfesor: String, // Columna 'nombre_profesor'
    val fecha: String,
    val codigoAcceso: String, // Columna 'codigo_acceso'
    val estado: String // 'activa' o 'finalizada'
)