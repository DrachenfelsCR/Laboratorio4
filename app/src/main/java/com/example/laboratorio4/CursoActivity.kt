package com.example.laboratorio4

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList

class CursoActivity : AppCompatActivity() {
    lateinit var lista: RecyclerView
    lateinit var adaptador: Cursos_RecyclerViewAdapter
    var position: Int = 0
    lateinit var curso: CursosItem
    var archived = ArrayList<CursosItem>()
    val oracleDao = OracleDAO.instance
    var cursos = CursosDAO.instance
    private lateinit var builder: AlertDialog.Builder

    private fun deleteCurso(cursoBorrar : CursosItem){

        oracleDao.deleteCurso(this,cursoBorrar.codigo)

        Toast.makeText(this, "Curso Borrado", Toast.LENGTH_LONG).show()

    }
    private fun getListOfCursos(codigoCarrera : String) {
        oracleDao.loadAllCursos(codigoCarrera)
        val newCursos = ArrayList<CursosItem>()
        for (c in cursos.getCursos()) {
            newCursos.add(c)
        }
        adaptador = Cursos_RecyclerViewAdapter(newCursos)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curso)
        lista = findViewById(R.id.listRecyclerViewCursos)
        builder = AlertDialog.Builder(this)
        val codigoCarrera = intent.getStringExtra("EXTRA_CARRERA")
        getListOfCursos(codigoCarrera.toString())

        val imgBtnRegistrarCurso = findViewById<ImageButton>(R.id.imgBtnRegistrarCiclo)

        imgBtnRegistrarCurso.setOnClickListener {
            val i = Intent(this, RegistrarCursoActivity::class.java)
            i.putExtra("EXTRA_CARRERA",codigoCarrera)
            startActivity(i)
        }

        findViewById<SearchView>(R.id.svNombreCurso).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPosition: Int = viewHolder.adapterPosition
                    val toPosition: Int = target.adapterPosition
                    Collections.swap(cursos.getCursos(), fromPosition, toPosition)
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val codigoCarrera = intent.getStringExtra("EXTRA_CARRERA")
                    position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.LEFT) {
                        curso = CursosItem(
                            cursos.getCursos()[position].codigo,
                            cursos.getCursos()[position].creditos,
                            cursos.getCursos()[position].horasPorSemana,
                            cursos.getCursos()[position].nombre
                        )
                        deleteCurso(curso)
                        adaptador = Cursos_RecyclerViewAdapter(cursos.getCursos())
                        lista.adapter?.notifyItemRemoved(position)
                    } else {
                        curso = CursosItem(
                            cursos.getCursos()[position].codigo,
                            cursos.getCursos()[position].creditos,
                            cursos.getCursos()[position].horasPorSemana,
                            cursos.getCursos()[position].nombre
                        )
                        archived.add(curso)
                        //cursos.deleteCurso(position)
                        //Este es el Intent que enviara a un formulario para editar al aplicante
                        Intent(this@CursoActivity,RegistrarGrupoActivity::class.java).also {
                          it.putExtra("EXTRA_CODIGOCURSO",curso.codigo)
                          startActivity(it)
                        }
                    }


                    getListOfCursos(codigoCarrera.toString())

                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                         dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean)
                {
                    RecyclerViewSwipeDecorator.Builder(this@CursoActivity, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@CursoActivity, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(this@CursoActivity, R.color.green))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(lista)
    }

    override fun onResume(){
        super.onResume()
        val codigoCarrera = intent.getStringExtra("EXTRA_CARRERA")
        getListOfCursos(codigoCarrera.toString())
    }
}