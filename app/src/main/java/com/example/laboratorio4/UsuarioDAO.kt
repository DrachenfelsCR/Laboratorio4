package com.example.laboratorio4

class UsuarioDAO {

        var usuarioDao = UsuarioDAO.instance

        private var usuario: Usuario = Usuario("","")

        init{

        }

        private object singletonUsuario {
            val INSTANCE = UsuarioDAO()
        }
        companion object {
        val instance: UsuarioDAO by lazy {
            singletonUsuario.INSTANCE
        }
    }

        fun getUsuario() : Usuario{
            return this?.usuario!!
        }

        fun setUsuario(usuario : Usuario) : String{
            this.usuario = usuario
            return "cargado"
        }

    }