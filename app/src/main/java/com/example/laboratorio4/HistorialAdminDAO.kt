package com.example.laboratorio4

class HistorialAdminDAO {
    var historialAdmin = HistorialAdminDAO.instance

    private var historialAdminLista: ArrayList<HistorialAdmin> = ArrayList<HistorialAdmin>()

    init{

    }

    private object singletonHistorialAdmin {
        val INSTANCE = HistorialAdminDAO()
    }
    companion object {
        val instance: HistorialAdminDAO by lazy {
            singletonHistorialAdmin.INSTANCE
        }
    }

    fun getHistorialAdminLista(): ArrayList<HistorialAdmin>{
        return this?.historialAdminLista!!
    }

    fun setHistorialAdminLista(arr : ArrayList<HistorialAdmin>) : String{
        this.historialAdminLista = arr
        return "cargado"
    }

}