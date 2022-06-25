package com.example.laboratorio4

class AlumnoGrupoDAO {
    var alumnoGrupoDao = HistorialAdminDAO.instance

    private var listaAlumnosGrupo: ArrayList<AlumnoGrupo> = ArrayList<AlumnoGrupo>()

    init{

    }

    private object singletonAlumnoGrupoDAO {
        val INSTANCE = AlumnoGrupoDAO()
    }
    companion object {
        val instance: AlumnoGrupoDAO by lazy {
            singletonAlumnoGrupoDAO.INSTANCE
        }
    }

    fun getAlumnosGrupos(): ArrayList<AlumnoGrupo>{
        return this?.listaAlumnosGrupo!!
    }

    fun setAlumnosGrupos(arr : ArrayList<AlumnoGrupo>) : String{
        this.listaAlumnosGrupo = arr
        return "cargado"
    }
}