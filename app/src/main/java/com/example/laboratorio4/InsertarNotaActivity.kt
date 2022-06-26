package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.gson.Gson
import org.json.JSONObject
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException

class InsertarNotaActivity : AppCompatActivity() {

    private lateinit var webSocketClient : WebSocketClient
    lateinit var datoNotaProfesor: DatoAlumnoNotaProfesor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_nota)
        datoNotaProfesor = intent.getSerializableExtra("EXTRA_DATONOTA") as DatoAlumnoNotaProfesor
        val codigoCurso = intent.getStringExtra("EXTRA_CODIGOCURSO").toString()
        createWebSocketClient()

        val imgBtnVolverInsertarNota = findViewById<ImageButton>(R.id.imgBtnVolverInsertarNota)
        val btnEnviarNota = findViewById<Button>(R.id.btnEnviarNota)
        val etIngresarNota = findViewById<EditText>(R.id.etIngresarNota)

        imgBtnVolverInsertarNota.setOnClickListener {
            finish()
        }

        btnEnviarNota.setOnClickListener {
            val nota = etIngresarNota.text.toString().toInt()
            etIngresarNota.text.clear()
            sendMessage(datoNotaProfesor,codigoCurso,nota)
            Thread.sleep(1000)
            Toast.makeText(this,"Nota insertada",Toast.LENGTH_SHORT).show()
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
                    Log.d("msg",s)
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
    fun sendMessage(al: DatoAlumnoNotaProfesor, codeCurso : String, nota : Int) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","UPDATENOTA")
        apiCall.put("idAlumno",al.IdAlumno)
        apiCall.put("codigoCurso",codeCurso)
        apiCall.put("Nota",nota)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }
}