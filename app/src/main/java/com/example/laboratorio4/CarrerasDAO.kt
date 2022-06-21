package com.example.laboratorio4

class CarrerasDAO {
    var carrerasDao = CarrerasDAO.instance

    private var carreras: ArrayList<CarrerasItem> = ArrayList<CarrerasItem>()

    init{

    }

    private object singletonCarreras {
        val INSTANCE = CarrerasDAO()
    }
    companion object {
        val instance: CarrerasDAO by lazy {
            singletonCarreras.INSTANCE
        }
    }

    fun addCarrera(carrera: CarrerasItem){
        carreras?.add(carrera)
    }

    fun getCarreras(): ArrayList<CarrerasItem>{
        return this?.carreras!!
    }

    fun setCarreras(arr : ArrayList<CarrerasItem>) : String{
        this.carreras = arr
        return "cargado"
    }

}