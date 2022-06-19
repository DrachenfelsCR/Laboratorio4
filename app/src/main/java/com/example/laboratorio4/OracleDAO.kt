package com.example.laboratorio4
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class OracleDAO {
    var carreras = CarrerasDAO.instance

    init {

    }
    private object singletonOracleDAO {
        val INSTANCE = OracleDAO()
    }
    companion object {
        val instance: OracleDAO by lazy {
            singletonOracleDAO.INSTANCE
        }
    }
    private val client = OkHttpClient()

    fun loadAllCarreras(){
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/Willyrex/api/carrera/getAll")
            .build()

        var countDownLatch: CountDownLatch = CountDownLatch(1)

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                  e.printStackTrace()
                    countDownLatch.countDown();
                }

                override fun onResponse(call: Call, responseHttp: okhttp3.Response) {
                    //Data
                    var valor = responseHttp.body()?.string()
                    val gson = Gson()
                    val entidadJson = gson.fromJson<Carreras>(valor, Carreras::class.java)
                    val arr = ArrayList<CarrerasItem>()
                    for (i in 0..entidadJson.size-1)
                    {
                        arr.add(entidadJson[i])
                    }
                    carreras.setCarreras(arr)
                    countDownLatch.countDown();
                }
        })
        countDownLatch.await();
    }



    suspend fun cargarCarrerasAsync(valor : String?) : String{
        val gson = Gson()
        val entidadJson = gson.fromJson<Carreras>(valor, Carreras::class.java)
        val arr = ArrayList<CarrerasItem>()
        for (i in 0..entidadJson.size-1)
        {
            arr.add(entidadJson[i])
        }
        carreras.setCarreras(arr)
        println(carreras)
        return "Ok"
    }

}