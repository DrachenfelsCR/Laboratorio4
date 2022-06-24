package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.recipes.WebSocketAdminHistorial
import org.json.JSONObject
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

class HistorialAlumnoAdminActivity : AppCompatActivity() {
    private lateinit var webSocketClient : WebSocketClient
    lateinit var lista: RecyclerView
    lateinit var adaptador: HistorialAdmin_RecyclerViewAdapter
    var position: Int = 0
    lateinit var historial : HistorialAdmin
    var archived = ArrayList<HistorialAdmin>()
    val oracleDao = OracleDAO.instance
    var historiales = HistorialAdminDAO.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_alumno_admin)
        lista = findViewById(R.id.listRecyclerViewHistorialAdmin)
        createWebSocketClient()

        val etAdminHistorialBuscar = findViewById<EditText>(R.id.etAdminHistorialBuscar)
        val btnAdminBuscarHistorial = findViewById<Button>(R.id.btnAdminBuscarHistorial)

       // getListOfHistorialesAdmin()

        btnAdminBuscarHistorial.setOnClickListener {
            sendMessage(etAdminHistorialBuscar.text.toString())
            Thread.sleep(1000)
            getListOfHistorialesAdmin()
        }
    }

    private fun createWebSocketClient() {
        val uri: URI
        try {
            // Connect to local host
            uri = URI("ws://10.0.2.2:8080/Willyrex/nota")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket", "Session is starting")
                //webSocketClient.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket", "Message received")
                    try {
                        val gson = Gson()
                        val entidadHistorial = gson.fromJson<EstudianteHistorialAdmin>(s, EstudianteHistorialAdmin::class.java)
                        historiales.setHistorialAdminLista(entidadHistorial.data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                //webSocketClient.close()
            }

            override fun onBinaryReceived(data: ByteArray) {}
            override fun onPingReceived(data: ByteArray) {}
            override fun onPongReceived(data: ByteArray) {}
            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                Log.i("WebSocket", "Closed ")
                println("onCloseReceived")
            }
        }
        webSocketClient.setConnectTimeout(10000)
        webSocketClient.setReadTimeout(60000)
        webSocketClient.enableAutomaticReconnection(5000)
        webSocketClient.connect()
    }

    fun sendMessage(valorBusqueda: String) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETHISTORIALPORID")
        apiCall.put("idAlumno",valorBusqueda)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }

    private fun getListOfHistorialesAdmin() {

        val newHistoriales = historiales.getHistorialAdminLista()

        adaptador = HistorialAdmin_RecyclerViewAdapter(newHistoriales)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }


}