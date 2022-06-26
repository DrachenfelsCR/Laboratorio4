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

class ProfesorAlumnosActivity : AppCompatActivity() {
    private lateinit var webSocketClient : WebSocketClient
    private lateinit var webSocketClient2 : WebSocketClient
    private lateinit var webSocketClient3 : WebSocketClient

    lateinit var lista: RecyclerView
    lateinit var adaptador: ProfesorAlumnos_RecyclerViewAdapter
    lateinit var datoNotaProfesor: DatoAlumnoNotaProfesor
    var position: Int = 0
    var archived = ArrayList<DatoAlumnoNotaProfesor>()
    val oracleDao = OracleDAO.instance
    val alumnosProfesorNota = DatoAlumnoProfesorDAO.instance
    val gruposDelProfesor = GruposDelProfesorDAO.instance
    var idGrupo : Int = 0
    lateinit var codigoCurso : String
    private lateinit var idProfesor : String


    private fun getListOfAlumnosNotas() {

        val newAlumnoGrupos = alumnosProfesorNota.getAlumnosProfesorDatos()

        adaptador = ProfesorAlumnos_RecyclerViewAdapter(newAlumnoGrupos)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }
    private fun resetListOfAlumnosNotas() {

        val newAlumnoGrupos =  ArrayList<DatoAlumnoNotaProfesor>()
        alumnosProfesorNota.setAlumnosProfesorDatos(ArrayList<DatoAlumnoNotaProfesor>())

    }
    override fun onResume() {
        super.onResume()
        resetListOfAlumnosNotas()
        getListOfAlumnosNotas()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesor_alumnos)
        lista = findViewById(R.id.listRecyclerViewProfesorNotas)
        idProfesor = intent.getStringExtra("EXTRA_IDPROFESOR").toString()
        val imgBtnVolverProfesorAlumnos =findViewById<ImageButton>(R.id.imgBtnVolverProfesorAlumnos)
        val etBuscarAlumnosPorIdGrupo = findViewById<EditText>(R.id.etBuscarAlumnosPorIdGrupo)
        val btnBuscarAlumnosPorGrupo = findViewById<Button>(R.id.btnBuscarAlumnosPorGrupo)
        val svBuscarProfesorAlumnoNota = findViewById<SearchView>(R.id.svBuscarProfesorAlumnoNota)
        val tvCodigoCursoNotas = findViewById<TextView>(R.id.tvCodigoCursoNotas)
        imgBtnVolverProfesorAlumnos.setOnClickListener { finish() }
        createWebSocketClient()
        createWebSocketClient2()

        btnBuscarAlumnosPorGrupo.setOnClickListener {
            val idGroup = etBuscarAlumnosPorIdGrupo.text.toString()?.toInt()
            sendMessage2(idProfesor)
            Thread.sleep(1000)
            val arrLength =  gruposDelProfesor.getGruposDelProfesor().size
            for (i in 0 until arrLength)
            {
                if (gruposDelProfesor.getGruposDelProfesor()[i].Id == idGroup){
                    tvCodigoCursoNotas.text = gruposDelProfesor.getGruposDelProfesor()[i].codigoCurso
                    codigoCurso = gruposDelProfesor.getGruposDelProfesor()[i].codigoCurso
                    sendMessage(idGroup)
                    Thread.sleep(1000)
                    getListOfAlumnosNotas()
                    break
                }
            }
        }

        findViewById<SearchView>(R.id.svBuscarProfesorAlumnoNota).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
                    Collections.swap(alumnosProfesorNota.getAlumnosProfesorDatos(), fromPosition, toPosition)
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {

                    } else {
                        datoNotaProfesor = DatoAlumnoNotaProfesor(
                            alumnosProfesorNota.getAlumnosProfesorDatos()[position].IdAlumno,
                            alumnosProfesorNota.getAlumnosProfesorDatos()[position].NombreAlumno,
                            alumnosProfesorNota.getAlumnosProfesorDatos()[position].Nota
                        )
                        Intent(this@ProfesorAlumnosActivity,InsertarNotaActivity::class.java).also {
                            it.putExtra("EXTRA_DATONOTA",datoNotaProfesor)
                            it.putExtra("EXTRA_CODIGOCURSO",codigoCurso)
                            startActivity(it)
                        }
                     //   sendMessageMatricular(alGrupo.Id)
                      //  sendMessage3(alGrupo)
                        archived.add(datoNotaProfesor)
                        //Este es el Intent que enviara a un formulario para editar al aplicante
                    }
                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                         dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
                {
                    RecyclerViewSwipeDecorator.Builder(this@ProfesorAlumnosActivity, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@ProfesorAlumnosActivity, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(this@ProfesorAlumnosActivity, R.color.green))
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
                    val entidadAlumnoGrupo = gson.fromJson<DataAlumnosNotaProfesor>(s, DataAlumnosNotaProfesor::class.java)
                    alumnosProfesorNota.setAlumnosProfesorDatos(entidadAlumnoGrupo.data)
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
            uri = URI("ws://10.0.2.2:8080/Willyrex/buscarGrupoPorId")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient2 = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket2", "Session is starting")
                //webSocketClient.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket2", "Message received")
                try {
                    val gson = Gson()
                    val entidadGruposdeprofesor = gson.fromJson<GruposDelProfesor>(s, GruposDelProfesor::class.java)
                    gruposDelProfesor.setGruposDelProfesor(entidadGruposdeprofesor.data)
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
                Log.i("WebSocket2", "Closed ")
                println("onCloseReceived")
            }
        }
        webSocketClient2.setConnectTimeout(10000)
        webSocketClient2.setReadTimeout(60000)
        webSocketClient2.enableAutomaticReconnection(5000)
        webSocketClient2.connect()
    }

    fun sendMessage(idgrupo: Int) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETALUMNOSPORIDGRUPO")
        apiCall.put("IdGrupo",idgrupo)
        val apiCallString = apiCall.toString()
        webSocketClient.send(apiCallString)
    }

    fun sendMessage2(idProfesor: String) {
        Log.i("WebSocket", "Button was clicked")
        val apiCall = JSONObject()
        apiCall.put("action","GETGRUPOSPORIDPROFESOR")
        apiCall.put("IdProfesor",idProfesor)
        val apiCallString = apiCall.toString()
        webSocketClient2.send(apiCallString)
    }
}