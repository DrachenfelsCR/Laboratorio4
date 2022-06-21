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

class CiclosActivity : AppCompatActivity() {

    lateinit var lista: RecyclerView
    lateinit var adaptador: Ciclos_RecyclerViewAdapter
    var position: Int = 0
    lateinit var curso: CiclosItem
    var archived = ArrayList<CiclosItem>()
    val oracleDao = OracleDAO.instance
    var ciclos = CiclosDAO.instance

    private fun getListOfCiclos() {
        oracleDao.loadAllCiclos()
        val newCiclos = ArrayList<CiclosItem>()
        for (c in ciclos.getCiclos()) {
            newCiclos.add(c)
        }
        adaptador = Ciclos_RecyclerViewAdapter(newCiclos)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(lista.context)
        lista.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciclos)
        lista = findViewById(R.id.listRecyclerViewCiclos)
        getListOfCiclos()

        val imgBtnCiclosVolver = findViewById<ImageButton>(R.id.imgBtnCiclosVolver)
        val imgBtnRegistrarCiclo = findViewById<ImageButton>(R.id.imgBtnRegistrarCiclo)

        imgBtnCiclosVolver.setOnClickListener { finish() }
        imgBtnRegistrarCiclo.setOnClickListener {
            val i = Intent(this, RegisterCicloActivity::class.java)
            startActivity(i)
        }

        findViewById<SearchView>(R.id.svAnnioCiclo).setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador.filter.filter(newText)
                return false
            }
        })

    }
    override fun onResume(){
        super.onResume()
        getListOfCiclos()
        adaptador.notifyDataSetChanged()
    }
}