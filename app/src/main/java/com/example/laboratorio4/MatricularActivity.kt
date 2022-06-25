package com.example.laboratorio4

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import org.json.JSONObject
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList

class MatricularActivity : AppCompatActivity() {
    private lateinit var webSocketClient : WebSocketClient
    private lateinit var webSocketClient2 : WebSocketClient
    private lateinit var webSocketClient3 : WebSocketClient

    lateinit var lista: RecyclerView
    lateinit var adaptador: Matricular_RecyclerViewAdapter
    var position: Int = 0
    lateinit var alGrupo : AlumnoGrupo
    var archived = ArrayList<AlumnoGrupo>()
    val oracleDao = OracleDAO.instance
    var alumnoGrupo = AlumnoGrupoDAO.instance
    lateinit var alumnoRecuperado : AlumnoMatricularBusqueda
    lateinit var alumnoId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matricular)
        lista = findViewById(R.id.listRecyclerViewMatricular)
        createWebSocketClient()
        createWebSocketClient2()
        createWebSocketClient3()
        val etMatricularBuscarEstudiante = findViewById<EditText>(R.id.etMatricularBuscarEstudiante)
        val btnBuscarAlumnoGrupoMatricular = findViewById<Button>(R.id.btnBuscarAlumnoGrupoMatricular)
        val svMatricularBuscarCurso = findViewById<SearchView>(R.id.svMatricularBuscarCurso)

        val tvEstudianteMatricularNombre = findViewById<TextView>(R.id.tvEstudianteMatricularNombre)
        val tvEstudianteMatricularId = findViewById<TextView>(R.id.tvEstudianteMatricularId)
        val tvEstudianteMatricularCodigoCarrera = findViewById<TextView>(R.id.tvEstudianteMatricularCodigoCarrera)

        val imgBtnVolverMatricular = findViewById<ImageButton>(R.id.imgBtnVolverMatricular)

        imgBtnVolverMatricular.setOnClickListener {
            finish()
        }

        btnBuscarAlumnoGrupoMatricular.setOnClickListener {
            sendMessage2(etMatricularBuscarEstudiante.text.toString())
            Thread.sleep(1000)
            tvEstudianteMatricularNombre.text = alumnoRecuperado.data.Nombre
            tvEstudianteMatricularId.text = "Id Alumno: " + alumnoRecuperado.data.Id
            tvEstudianteMatricularCodigoCarrera.text = "Carrera: " + alumnoRecuperado.data.CodigoCarrera
            sendMessage(alumnoRecuperado.data.CodigoCarrera)
            Thread.sleep(1000)
            getListOfAlumnoGrupos()
        }

        findViewById<SearchView>(R.id.svMatricularBuscarCurso).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador.filter.filter(newText)
                return false
            }
        })

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPosition: Int = viewHolder.adapterPosition
                    val toPosition: Int = target.adapterPosition
                    Collections.swap(alumnoGrupo.getAlumnosGrupos(), fromPosition, toPosition)
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {

                    } else {
                        alGrupo = AlumnoGrupo(
                            alumnoGrupo.getAlumnosGrupos()[position].CodigoCurso,
                            alumnoGrupo.getAlumnosGrupos()[position].HoraFin,
                            alumnoGrupo.getAlumnosGrupos()[position].HoraInicio,
                            alumnoGrupo.getAlumnosGrupos()[position].Id,
                            alumnoGrupo.getAlumnosGrupos()[position].IdCiclo,
                            alumnoGrupo.getAlumnosGrupos()[position].IdProfesor,
                            alumnoGrupo.getAlumnosGrupos()[position].NumeroGrupo
                        )
                        sendMessageMatricular(alGrupo.Id)
                        sendMessage3(alGrupo)
                        archived.add(alGrupo)
                        //Este es el Intent que enviara a un formulario para editar al aplicante
                        Snackbar.make(lista, "se ha matriculo en grupo "+alGrupo.NumeroGrupo , Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                         dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
                {
                    RecyclerViewSwipeDecorator.Builder(this@MatricularActivity, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@MatricularActivity, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(this@MatricularActivity, R.color.green))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_control_point_24)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(lista)
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
                    val gson = Gson()
                    val entidadAlumnoGrupo = gson.fromJson<DataMatricularAlumno>(s, DataMatricularAlumno::class.java)
                    alumnoGrupo.setAlumnosGrupos(entidadAlumnoGrupo.data)
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

    private fun createWebSocketClient2() {
        val uri: URI
        try {
            // Connect to local host
            uri = URI("ws://10.0.2.2:8080/Willyrex/buscarAlumnoPorId")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient2 = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket", "Session is starting")
                //webSocketClient.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket", "Message received")
                try {
                    val gson = Gson()
                    val entidadAlumnoGrupo = gson.fromJson<AlumnoMatricularBusqueda>(s, AlumnoMatricularBusqueda::class.java)
                    alumnoRecuperado = entidadAlumnoGrupo
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
        webSocketClient2.setConnectTimeout(10000)
        webSocketClient2.setReadTimeout(60000)
        webSocketClient2.enableAutomaticReconnection(5000)
        webSocketClient2.connect()
    }
    private fun createWebSocketClient3() {
        val uri: URI
        try {
            // Connect to local host
            uri = URI("ws://10.0.2.2:8080/Willyrex/nota")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient3 = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket3", "Session is starting")
                //webSocketClient.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket3", "Message received")
                try {
                 Log.d("msg",s)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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
        webSocketClient3.setConnectTimeout(10000)
        webSocketClient3.setReadTimeout(90000)
        webSocketClient3.enableAutomaticReconnection(5000)
        webSocketClient3.connect()
    }


    fun sendMessage(valorBusqueda: String) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETGRUPOSPORCARRERA")
        apiCall.put("idCarrera",valorBusqueda)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }
    fun sendMessageMatricular(idGrupo: Int) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()

        apiCall.put("action","MATRICULAR")
        apiCall.put("Id",alumnoRecuperado.data.Id)
        apiCall.put("Nombre",alumnoRecuperado.data.Nombre)
        apiCall.put("IdGrupo",idGrupo)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }
    fun sendMessage2(valorBusqueda: String) {
        Log.i("WebSocket2", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETALUMNOID")
        apiCall.put("id",valorBusqueda)
        val apiCallString = apiCall.toString()
        webSocketClient2.send(apiCallString)
    }
    fun sendMessage3(al: AlumnoGrupo) {
        Log.i("WebSocket3", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","REGISTER")
        apiCall.put("idCiclo",al.IdCiclo)
        apiCall.put("CodigoCurso",al.CodigoCurso)
        apiCall.put("idProfesor",al.IdProfesor)
        apiCall.put("idAlumno",alumnoRecuperado.data.Id)
        apiCall.put("Nota",0)
        val apiCallString = apiCall.toString()
        webSocketClient3.send(apiCallString)
    }

    private fun getListOfAlumnoGrupos() {

        val newAlumnoGrupos = alumnoGrupo.getAlumnosGrupos()

        adaptador = Matricular_RecyclerViewAdapter(newAlumnoGrupos)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }
}