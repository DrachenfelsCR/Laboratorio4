package com.example.laboratorio4

class CiclosDAO {
    var ciclosDao = CiclosDAO.instance

    private var ciclos: ArrayList<CiclosItem> = ArrayList<CiclosItem>()

    init{

    }

    private object singletonCiclos {
        val INSTANCE = CiclosDAO()
    }
    companion object {
        val instance: CiclosDAO by lazy {
            singletonCiclos.INSTANCE
        }
    }

    fun addCiclo(ciclo: CiclosItem){
        ciclos?.add(ciclo)
    }

    fun getCiclos(): ArrayList<CiclosItem>{
        return this?.ciclos!!
    }

    fun setCiclos(arr : ArrayList<CiclosItem>) : String{
        this.ciclos = arr
        return "cargado"
    }

}