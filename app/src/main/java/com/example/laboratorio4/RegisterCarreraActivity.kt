package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegisterCarreraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_carrera)

        val imgBtnRegistrarCarreraAtras = findViewById<ImageButton>(R.id.imgBtnRegistrarCarreraAtras)
        val btnSubmitRegistrarCarrera = findViewById<Button>(R.id.btnSubmitRegistrarCarrera)

        val etRegistrarCarreraCodigo = findViewById<EditText>(R.id.etRegistrarCarreraCodigo)
        val etRegistrarCarreraNombre = findViewById<EditText>(R.id.etRegistrarCarreraNombre)
        val etRegistrarCarreraTitulo = findViewById<EditText>(R.id.etRegistrarCarreraTitulo)

        val oracleDao = OracleDAO.instance

        imgBtnRegistrarCarreraAtras.setOnClickListener {
            finish()
        }

        btnSubmitRegistrarCarrera.setOnClickListener {
            if (etRegistrarCarreraCodigo.text.isEmpty() || etRegistrarCarreraNombre.text.isEmpty() || etRegistrarCarreraTitulo.text.isEmpty()){
                Toast.makeText(this,"Hay campos vacios en el formulario", Toast.LENGTH_SHORT)
            }
            else{
                val nCarrera = CarrerasItem(
                    etRegistrarCarreraCodigo.text.toString(),
                    etRegistrarCarreraNombre.text.toString(),
                    etRegistrarCarreraTitulo.text.toString()
                )
                etRegistrarCarreraCodigo.text.clear()
                etRegistrarCarreraNombre.text.clear()
                etRegistrarCarreraTitulo.text.clear()
                oracleDao.postCarrera(this,nCarrera)
                Toast.makeText(this,"INSERTADO EXITOSAMENTE", Toast.LENGTH_SHORT)
                finish()
            }
        }
    }
}