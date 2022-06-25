package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.json.JSONObject
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException

class EstudianteHistorialActivity : AppCompatActivity() {
    private lateinit var webSocketClient : WebSocketClient

    lateinit var lista: RecyclerView
    lateinit var adaptador: Historial_RecyclerViewAdapter
    var position: Int = 0
    lateinit var historial : HistorialAdmin
    var archived = ArrayList<HistorialAdmin>()
    val oracleDao = OracleDAO.instance
    var historiales = HistorialDAO.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiante_historial)
        lista = findViewById(R.id.listRecyclerViewHistorialAcademico)

        val userId = intent.getStringExtra("EXTRA_IDALUMNO").toString()
        createWebSocketClient()
        val btnBuscarHistorial = findViewById<Button>(R.id.btnBuscarHistorial)
        val etHistorialAcademicoCiclo = findViewById<EditText>(R.id.etHistorialAcademicoCiclo)

        btnBuscarHistorial.setOnClickListener {
            val ciclo = etHistorialAcademicoCiclo.text.toString()
            sendMessage(userId,ciclo)
            Thread.sleep(1000)
            getListOfHistoriales()
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
                    historiales.setHistorialLista(entidadHistorial.data)
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

    fun sendMessage(idalumno: String,idciclo: String) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETHISTORIAL")
        apiCall.put("idAlumno",idalumno)
        apiCall.put("idCiclo",idciclo)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }

    private fun getListOfHistoriales() {

        val newHistoriales = historiales.getHistorialLista()

        adaptador = Historial_RecyclerViewAdapter(newHistoriales)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }
}