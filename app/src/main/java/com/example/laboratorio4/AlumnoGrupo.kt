package com.example.laboratorio4

import java.io.Serializable

data class AlumnoGrupo(
    val CodigoCurso: String,
    val HoraFin: String,
    val HoraInicio: String,
    val Id: Int,
    val IdCiclo: Int,
    val IdProfesor: String,
    val NumeroGrupo: Int
): Serializable