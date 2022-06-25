package com.example.laboratorio4

class GruposDelProfesorDAO {


    private var DatosGruposDelProfesor: ArrayList<GrupoProfesor> = ArrayList<GrupoProfesor>()

    init{

    }

    private object singletonGruposDelProfesorDAO {
        val INSTANCE = GruposDelProfesorDAO()
    }
    companion object {
        val instance: GruposDelProfesorDAO by lazy {
            singletonGruposDelProfesorDAO.INSTANCE
        }
    }

    fun getGruposDelProfesor(): ArrayList<GrupoProfesor>{
        return this?.DatosGruposDelProfesor!!
    }

    fun setGruposDelProfesor(arr : ArrayList< GrupoProfesor>) : String{
        this.DatosGruposDelProfesor = arr
        return "cargado"
    }

}