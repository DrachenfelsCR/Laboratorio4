package com.example.laboratorio4

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.lifecycleScope

class CarreraActivity : AppCompatActivity() {

    lateinit var lista: RecyclerView
    lateinit var adaptador: Carreras_RecyclerViewAdapter
    var position: Int = 0
    lateinit var carrera: CarrerasItem
    var archived = ArrayList<CarrerasItem>()
    val oracleDao = OracleDAO.instance
    var carreras = CarrerasDAO.instance



    private fun getListOfCarreras() {
        oracleDao.loadAllCarreras()
        val newCarreras = ArrayList<CarrerasItem>()

        for (c in carreras.getCarreras()) {
            newCarreras.add(c)
        }
        adaptador = Carreras_RecyclerViewAdapter(newCarreras)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }

    override fun onResume(){
        super.onResume()
        getListOfCarreras()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrera)
        lista = findViewById(R.id.listRecyclerViewCarreras)
        getListOfCarreras()

        val imgBtnAddCarrera = findViewById<ImageButton>(R.id.imgBtnAddCarrera)

        imgBtnAddCarrera.setOnClickListener {
            val i = Intent(this, RegisterCarreraActivity::class.java)
            startActivity(i)
        }

        findViewById<SearchView>(R.id.svNombreCarrera).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
                    Collections.swap(carreras.getCarreras(), fromPosition, toPosition)
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {
                        carrera = CarrerasItem(
                            carreras.getCarreras()[position].codigo,
                            carreras.getCarreras()[position].nombre,
                            carreras.getCarreras()[position].titulo
                        )
                        //cursos.deleteCurso(position)
                        //DBHelper.borrarCurso(curso._id) AQUI PONER FUN DE BORRAR
                        lista.adapter?.notifyItemRemoved(position)
                        Snackbar.make(lista, carrera.nombre + "Ha sido eliminado...", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                carreras.getCarreras().add(position, carrera)
                                lista.adapter?.notifyItemInserted(position)
                            }.show()
                        adaptador = Carreras_RecyclerViewAdapter(carreras.getCarreras())
                        lista.adapter = adaptador
                    } else {
                        carrera = CarrerasItem(
                            carreras.getCarreras()[position].codigo,
                            carreras.getCarreras()[position].nombre,
                            carreras.getCarreras()[position].titulo
                        )
                        archived.add(carrera)
                        //cursos.deleteCurso(position)
                        lista.adapter?.notifyItemRemoved(position)
                        //Este es el Intent que enviara a un formulario para editar al aplicante
                        Intent(this@CarreraActivity,CursoActivity::class.java).also {
                            it.putExtra("EXTRA_CARRERA",carrera.codigo)
                            it.putExtra("EXTRA_POSITION",position)
                            startActivity(it)
                        }
                        Snackbar.make(lista, carrera.nombre + " se editara...", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                archived.removeAt(archived.lastIndexOf(carrera))
                                lista.adapter?.notifyItemInserted(position)
                            }.show()
                        adaptador = Carreras_RecyclerViewAdapter(carreras.getCarreras())
                        lista.adapter = adaptador
                    }
                    getListOfCarreras()
                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                         dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
                {
                    RecyclerViewSwipeDecorator.Builder(this@CarreraActivity, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@CarreraActivity, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(this@CarreraActivity, R.color.green))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(lista)
    }




}