package com.example.laboratorio4

class CursosDAO {
    var cursosDao = CursosDAO.instance

    private var cursos: ArrayList<CursosItem> = ArrayList<CursosItem>()

    init{

    }

    private object singletonCursos {
        val INSTANCE = CursosDAO()
    }
    companion object {
        val instance: CursosDAO by lazy {
            singletonCursos.INSTANCE
        }
    }

    fun addCurso(curso: CursosItem){
        cursos?.add(curso)
    }

    fun getCursos(): ArrayList<CursosItem>{
        return this?.cursos!!
    }

    fun setCursos(arr : ArrayList<CursosItem>) : String{
        this.cursos = arr
        return "cargado"
    }

}