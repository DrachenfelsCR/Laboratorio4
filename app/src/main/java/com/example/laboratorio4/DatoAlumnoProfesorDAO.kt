package com.example.laboratorio4

class DatoAlumnoProfesorDAO {
    var DatosAlumnoDao = DatoAlumnoProfesorDAO.instance

    private var DatosAlumnos: ArrayList<DatoAlumnoNotaProfesor> = ArrayList<DatoAlumnoNotaProfesor>()

    init{

    }

    private object singletonDatoAlumnoNotaProfesor {
        val INSTANCE = DatoAlumnoProfesorDAO()
    }
    companion object {
        val instance: DatoAlumnoProfesorDAO by lazy {
            singletonDatoAlumnoNotaProfesor.INSTANCE
        }
    }

    fun getAlumnosProfesorDatos(): ArrayList<DatoAlumnoNotaProfesor>{
        return this?.DatosAlumnos!!
    }

    fun setAlumnosProfesorDatos(arr : ArrayList< DatoAlumnoNotaProfesor>) : String{
        this.DatosAlumnos = arr
        return "cargado"
    }

}