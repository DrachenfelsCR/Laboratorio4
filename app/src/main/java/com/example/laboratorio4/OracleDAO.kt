package com.example.laboratorio4

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.cfsuman.jetpack.VolleySingleton
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch


class OracleDAO {
    var carreras = CarrerasDAO.instance
    var cursos = CursosDAO.instance
    var ciclos = CiclosDAO.instance
    var usuario = UsuarioDAO.instance
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
    fun postCiclo(con : Context, ciclo : CiclosItem){
        val url = " http://10.0.2.2:8080/Willyrex/api/ciclo"
        // Post parameters
        // Form fields and values

        val jsonObject = JSONObject()
        jsonObject.put("numeroCiclo",ciclo.numeroCiclo)
        jsonObject.put("titulo",ciclo.titulo)
        jsonObject.put("annio",ciclo.annio)
        jsonObject.put("fechaFin",ciclo.fechaFin)
        jsonObject.put("fechaInicio",ciclo.fechaInicio)

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

    fun postCurso(con : Context, curso : CursosItem, codigoCarrera: String){
        var url = "http://10.0.2.2:8080/Willyrex/api/curso"

        val jsonObject = JSONObject()
        jsonObject.put("codigo",curso.codigo)
        jsonObject.put("nombre",curso.nombre)
        jsonObject.put("creditos",curso.creditos)
        jsonObject.put("horasPorSemana",curso.horasPorSemana)
        jsonObject.put("codigoCarrera",codigoCarrera)
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

    fun postEstudiante(con : Context,alumno:AlumnoCreacion){
        var url = "http://10.0.2.2:8080/Willyrex/api/users/addEstudiante"

        val jsonObject = JSONObject()
        jsonObject.put("id",alumno.id)
        jsonObject.put("nombre",alumno.nombre)
        jsonObject.put("password",alumno.password)
        jsonObject.put("codigoCarrera",alumno.codigoCarrera)
        jsonObject.put("email",alumno.email)
        jsonObject.put("fechaNacimiento",alumno.fechaNacimiento)
        jsonObject.put("rol",alumno.rol)
        jsonObject.put("telefono",alumno.telefono)
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            { response ->
                // Process the json
                try {
                    Log.d("Succesful", "Response: $response")
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

    fun postUsuario(con : Context,user:UsuarioCreacion){
        var url = "http://10.0.2.2:8080/Willyrex/api/users/addUser"

        val jsonObject = JSONObject()
        jsonObject.put("id",user.id)
        jsonObject.put("rol",user.rol)
        jsonObject.put("password",user.password)

        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            { response ->
                // Process the json
                try {
                    Log.d("Succesful", "Response: $response")
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
    fun postProfesor(con : Context,profesor:ProfesorCreacion){
        var url = "http://10.0.2.2:8080/Willyrex/api/users/addProfesor"

        val jsonObject = JSONObject()
        jsonObject.put("id",profesor.id)
        jsonObject.put("nombre",profesor.nombre)
        jsonObject.put("password",profesor.password)
        jsonObject.put("rol",profesor.rol)
        jsonObject.put("telefono",profesor.telefono)
        jsonObject.put("email",profesor.email)

        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            { response ->
                // Process the json
                try {
                    Log.d("Succesful", "Response: $response")
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

    fun deleteCurso(con: Context, curso: String){
        var url = "http://10.0.2.2:8080/Willyrex/api/curso/borrarCurso/$curso"
        var countDownLatch: CountDownLatch = CountDownLatch(1)
        val request = StringRequest(Request.Method.DELETE,url,
            { response ->
                // Process the json
                try {
                    Log.d("Succesful", "Response: ${response}")
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
    fun loadAllCiclos(){
        val client = OkHttpClient.Builder().build()
        val request = okhttp3.Request.Builder()
            .url("http://10.0.2.2:8080/Willyrex/api/ciclo/todos")
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
                val entidadJson = gson.fromJson<Ciclos>(valor, Ciclos::class.java)
                val arr = ArrayList<CiclosItem>()
                for (i in 0..entidadJson.size-1)
                {
                    arr.add(entidadJson[i])
                }
                ciclos.setCiclos(arr)

                countDownLatch.countDown();
            }
        })
        countDownLatch.await();
    }


    fun Login(con: Context, password: String, nombre: String){
        var url = "http://10.0.2.2:8080/Willyrex/api/login/sesion"

        val jsonObject = JSONObject()
        jsonObject.put("password",password)
        jsonObject.put("nombre",nombre)

        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            { response ->
                // Process the json
                try {
                    Log.d("Succesful", "Response: $response")
                    var returnedUser = Usuario(response.get("nombre").toString(),response.get("rol").toString())
                    usuario.setUsuario(returnedUser)

                }catch (e:Exception){
                    Log.d("Error", "Exception: ${e.message.toString()}")
                    var returnedUser = Usuario("Error","Error")
                    usuario.setUsuario(returnedUser)
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
        Thread.sleep(1000)
    }
}