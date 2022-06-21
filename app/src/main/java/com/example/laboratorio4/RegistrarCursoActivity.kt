package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegistrarCursoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_curso)

        val etRegistrarCursoCodigo = findViewById<EditText>(R.id.etRegistrarCursoCodigo)
        val etRegistrarCursoNombre = findViewById<EditText>(R.id.etRegistrarCursoNombre)
        val etRegistrarCursoCreditos = findViewById<EditText>(R.id.etRegistrarCursoCreditos)
        val etRegistrarCursoHoras = findViewById<EditText>(R.id.etRegistrarCursoHoras)

        val btnRegistrarCurso = findViewById<Button>(R.id.btnRegistrarCurso)
        val imgBtnRegistrarCursoVolver =  findViewById<ImageButton>(R.id.imgBtnRegistrarCursoVolver)

        val oracleDao = OracleDAO.instance

        imgBtnRegistrarCursoVolver.setOnClickListener {
            finish()
        }
        btnRegistrarCurso.setOnClickListener {
            val cursoCreditos =etRegistrarCursoCreditos.text.toString().toIntOrNull()
            val cursoHoras = etRegistrarCursoHoras.text.toString().toIntOrNull()
            if (etRegistrarCursoCodigo.text.isEmpty() ||  etRegistrarCursoNombre.text.isEmpty()
                || etRegistrarCursoCreditos.text.isEmpty() || etRegistrarCursoHoras.text.isEmpty()){

                Toast.makeText(this,"Hay espacios vacios en el formulario",Toast.LENGTH_SHORT).show()
            }
            else{
                if (cursoCreditos == null || cursoHoras == null){
                    Toast.makeText(this,"Creditos y Horas solo aceptan numeros",Toast.LENGTH_SHORT).show()
                }
                else{
                    val codigo = etRegistrarCursoCodigo.text.toString()
                    val nombre = etRegistrarCursoNombre.text.toString()
                    val creditos = etRegistrarCursoCreditos.text.toString().toInt()
                    val horas = etRegistrarCursoHoras.text.toString().toInt()

                    val ncurso = CursosItem(codigo,creditos,horas,nombre)
                    etRegistrarCursoCodigo.text.clear()
                    etRegistrarCursoNombre.text.clear()
                    etRegistrarCursoCreditos.text.clear()
                    etRegistrarCursoHoras.text.clear()
                    val codigoCarrera = intent.getStringExtra("EXTRA_CARRERA")
                    oracleDao.postCurso(this, ncurso, codigoCarrera.toString())
                    Toast.makeText(this,"INSERTADO EXITOSAMENTE", Toast.LENGTH_SHORT)
                    finish()
                }
            }

        }
    }
}