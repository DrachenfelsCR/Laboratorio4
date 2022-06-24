package com.example.laboratorio4

import java.io.Serializable

data class HistorialAdmin(
    val CodigoCurso: String,
    val IdProfesor: String,
    val Nombre: String,
    val Nota: Int
): Serializable