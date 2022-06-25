package com.example.laboratorio4

class HistorialDAO {
    var historial = HistorialDAO.instance

    private var historialLista: ArrayList<HistorialAdmin> = ArrayList<HistorialAdmin>()

    init{

    }

    private object singletonHistorial {
        val INSTANCE = HistorialDAO()
    }
    companion object {
        val instance: HistorialDAO by lazy {
            singletonHistorial.INSTANCE
        }
    }

    fun getHistorialLista(): ArrayList<HistorialAdmin>{
        return this?.historialLista!!
    }

    fun setHistorialLista(arr : ArrayList<HistorialAdmin>) : String{
        this.historialLista = arr
        return "cargado"
    }

}