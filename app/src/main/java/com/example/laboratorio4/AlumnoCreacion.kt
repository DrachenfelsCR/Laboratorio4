package com.example.laboratorio4
import  java.io.Serializable

data class AlumnoCreacion(
    val id: String,
    val nombre: String,
    val password: String,
    val rol: String,
    val telefono: String,
    val email: String,
    val fechaNacimiento: String,
    val codigoCarrera: String
): Serializable