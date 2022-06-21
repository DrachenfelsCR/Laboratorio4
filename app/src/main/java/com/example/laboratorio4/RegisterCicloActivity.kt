package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegisterCicloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ciclo)

        val imgBtnRegistrarCicloVolver = findViewById<ImageButton>(R.id.imgBtnRegistrarCicloVolver)
        val btnRegistrarCicloEnviar = findViewById<Button>(R.id.btnRegistrarCicloEnviar)

        val etCicloRegistrarAnnio = findViewById<EditText>(R.id.etCicloRegistrarAnnio)
        val etCicloRegistrarNCiclo = findViewById<EditText>(R.id.etCicloRegistrarNCiclo)
        val etCicloRegistrarFechaInicio = findViewById<EditText>(R.id.etCicloRegistrarFechaInicio)
        val etCicloRegistrarFechaFin = findViewById<EditText>(R.id.etCicloRegistrarFechaFin)
        val etCicloRegistrarTitulo = findViewById<EditText>(R.id.etCicloRegistrarTitulo)

        val oracleDao = OracleDAO.instance

        imgBtnRegistrarCicloVolver.setOnClickListener {
            finish()
        }

        btnRegistrarCicloEnviar.setOnClickListener {
            if (etCicloRegistrarAnnio.text.isEmpty() || etCicloRegistrarNCiclo.text.isEmpty() ||etCicloRegistrarFechaInicio.text.isEmpty()
                || etCicloRegistrarFechaFin.text.isEmpty()  || etCicloRegistrarTitulo.text.isEmpty()) {
                Toast.makeText(this,"Hay espacios vacios en el formulario", Toast.LENGTH_SHORT).show()
            }
            else{
                val annio = etCicloRegistrarAnnio.text.toString()
                val nCiclo = etCicloRegistrarNCiclo.text.toString()
                val regFechaInicio = etCicloRegistrarFechaInicio.text.toString()
                val regFechaFin =etCicloRegistrarFechaFin.text.toString()
                val titulo = etCicloRegistrarTitulo.text.toString()

                val newCiclo = CiclosItem(annio.toInt(),regFechaFin,regFechaInicio,0,nCiclo.toInt(),titulo)
                etCicloRegistrarAnnio.text.clear()
                etCicloRegistrarNCiclo.text.clear()
                etCicloRegistrarFechaInicio.text.clear()
                etCicloRegistrarFechaFin.text.clear()
                etCicloRegistrarTitulo.text.clear()
                oracleDao.postCiclo(this,newCiclo)
                Toast.makeText(this,"Ciclo Insertado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}