package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegistrarProfesorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_profesor)

        val imgBtnVolverRegistrarProfesor = findViewById<ImageButton>(R.id.imgBtnVolverRegistrarProfesor)

        val btnSubmitProfessor  = findViewById<Button>(R.id.btnSubmitProfessor)

        val etRegistrarProfesorId = findViewById<EditText>(R.id.etRegistrarProfesorId)
        val etRegistrarProfesorNombre = findViewById<EditText>(R.id.etRegistrarProfesorNombre)
        val etRegistrarProfesorPassword = findViewById<EditText>(R.id.etRegistrarProfesorPassword)
        val etRegistrarProfesorTelefono = findViewById<EditText>(R.id.etRegistrarProfesorTelefono)
        val etRegistrarProfesorEmail = findViewById<EditText>(R.id.etRegistrarProfesorEmail)

        val oracleDao = OracleDAO.instance

        imgBtnVolverRegistrarProfesor.setOnClickListener {
            finish()
        }
        btnSubmitProfessor.setOnClickListener {
            if (etRegistrarProfesorId.text.isEmpty() || etRegistrarProfesorNombre.text.isEmpty() || etRegistrarProfesorPassword.text.isEmpty()
                || etRegistrarProfesorTelefono.text.isEmpty() || etRegistrarProfesorEmail.text.isEmpty()){
                Toast.makeText(this,"Hay espacios vacios", Toast.LENGTH_SHORT).show()
            }
            else{
                val id = etRegistrarProfesorId.text.toString()
                val nombre = etRegistrarProfesorNombre.text.toString()
                val password = etRegistrarProfesorPassword.text.toString()
                val rol = "Profesor"
                val telefono = etRegistrarProfesorTelefono.text.toString()
                val email = etRegistrarProfesorEmail.text.toString()
                val profesorCreado = ProfesorCreacion(id,nombre,password,rol,telefono,email)
                oracleDao.postProfesor(this,profesorCreado)
                etRegistrarProfesorId.text.clear()
                etRegistrarProfesorNombre.text.clear()
                etRegistrarProfesorPassword.text.clear()
                etRegistrarProfesorTelefono.text.clear()
                etRegistrarProfesorEmail.text.clear()
                Toast.makeText(this,"Profesor Insertado", Toast.LENGTH_SHORT).show()
            }
        }

    }
}