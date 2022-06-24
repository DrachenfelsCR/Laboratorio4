package com.example.laboratorio4

import java.io.Serializable

data class UsuarioCreacion(
   val id: String,
  val password: String,
   val rol: String
): Serializable