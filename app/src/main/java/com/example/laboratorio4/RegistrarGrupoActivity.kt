package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import org.json.JSONObject
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException

class RegistrarGrupoActivity : AppCompatActivity() {
    private lateinit var webSocketClient : WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_grupo)

        val etRegistrarGrupoIdProfesor = findViewById<EditText>(R.id.etRegistrarGrupoIdProfesor)
        val etRegistrarGrupoIdCiclo = findViewById<EditText>(R.id.etRegistrarGrupoIdCiclo)
        val etRegistrarGrupoHoraInicio = findViewById<EditText>(R.id.etRegistrarGrupoHoraInicio)
        val etRegistrarGrupoHoraFin = findViewById<EditText>(R.id.etRegistrarGrupoHoraFin)

        val btnRegistrarGrupo = findViewById<Button>(R.id.btnRegistrarGrupo)

        btnRegistrarGrupo.setOnClickListener {
            if (etRegistrarGrupoIdProfesor.text.isEmpty() || etRegistrarGrupoIdCiclo.text.isEmpty() ||
                etRegistrarGrupoHoraInicio.text.isEmpty() || etRegistrarGrupoHoraFin.text.isEmpty()){
                Toast.makeText(this,"Hay espacios vacios", Toast.LENGTH_SHORT)
            }
            else{
                val profesor = etRegistrarGrupoIdProfesor.text.toString()
                val idCiclo = etRegistrarGrupoIdCiclo.text.toString()
                val horaInicio = etRegistrarGrupoHoraInicio.text.toString()
                val horaFinal = etRegistrarGrupoHoraFin.text.toString()
                val codigoCurso = intent.getStringExtra("EXTRA_CODIGOCURSO").toString()

                val json = JSONObject()
                json.put("profesor",profesor)
                json.put("idCiclo",idCiclo)
                json.put("horaInicio",horaInicio)
                json.put("horaFinal",horaFinal)
                json.put("codigoCurso",codigoCurso)
                sendMessage(json)
                Toast.makeText(this,"Grupo Insertado",Toast.LENGTH_LONG)
            }
        }
    }
    private fun createWebSocketClient() {
        val uri: URI
        try {
            // Connect to local host
            uri = URI("ws://10.0.2.2:8080/Willyrex/buscarGrupoPorId")
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
                    Log.d("Websocket",s)
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
    fun sendMessage(jsonObject: JSONObject) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","POSTGRUPO")
        apiCall.put("profesor",jsonObject.get("profesor"))
        apiCall.put("idCiclo",jsonObject.get("idCiclo"))
        apiCall.put("codigoCurso",jsonObject.get("codigoCurso"))
        apiCall.put("horaInicio",jsonObject.get("horaInicio"))
        apiCall.put("horaFinal",jsonObject.get("horaFinal"))
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }
}