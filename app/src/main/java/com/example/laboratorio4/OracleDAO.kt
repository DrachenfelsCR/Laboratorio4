package com.example.laboratorio4
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.cfsuman.jetpack.VolleySingleton
import org.json.JSONObject
import ru.gildor.coroutines.okhttp.await

class OracleDAO {
    var carreras = CarrerasDAO.instance
    var cursos = CursosDAO.instance

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

    fun postCarrera(con : Context, carrera : CarrerasItem){
        val url = "http://10.0.2.2:8080/Willyrex/api/carrera"
        // Post parameters
        // Form fields and values

        val jsonObject = JSONObject()
        jsonObject.put("codigo",carrera.codigo)
        jsonObject.put("nombre",carrera.nombre)
        jsonObject.put("titulo",carrera.titulo)
        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            { response ->
                // Process the json
                try {
                  Log.d("Succesful", "Response: ${response.get("Status")}")
                }catch (e:Exception){
                    Log.d("Error", "Exception: ${e.message.toString()}")
                }
            }, {
                // Error in request

                Log.d("VolleyError", "Volley error: $it")
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add the volley post request to the request queue
        VolleySingleton.getInstance(con).addToRequestQueue(request)

    }


    fun loadAllCarreras(){
        val client = OkHttpClient.Builder().build()
        val request = okhttp3.Request.Builder()
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


   // http://localhost:8080/Willyrex/api/curso/codigoCarrera/INF
    fun loadAllCursos(codigoCarrera : String){
       val client = OkHttpClient.Builder().build()
        val request = okhttp3.Request.Builder()
            .url("http://10.0.2.2:8080/Willyrex/api/curso/codigoCarrera/$codigoCarrera")
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
                val entidadJson = gson.fromJson<Cursos>(valor, Cursos::class.java)
                val arr = ArrayList<CursosItem>()
                for (i in 0..entidadJson.size-1)
                {
                    arr.add(entidadJson[i])
                }
                cursos.setCursos(arr)

                countDownLatch.countDown();
            }
        })
        countDownLatch.await();
    }
}