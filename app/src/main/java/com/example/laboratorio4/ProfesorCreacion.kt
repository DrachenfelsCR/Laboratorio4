package com.example.laboratorio4

import java.io.Serializable

data class ProfesorCreacion(
    val id: String,
    val nombre: String,
    val password: String,
    val rol: String,
    val telefono: String,
    val email: String
): Serializable