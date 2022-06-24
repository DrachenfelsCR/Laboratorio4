package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegisterEstudianteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_estudiante)

        val etRegistrarEstudianteId = findViewById<EditText>(R.id.etRegistrarEstudianteId)

        val etRegistrarEstudianteNombre = findViewById<EditText>(R.id.etRegistrarEstudianteNombre)
        val etRegistrarEstudiantePassword = findViewById<EditText>(R.id.etRegistrarEstudiantePassword)
        val etRegistrarEstudianteTelefono = findViewById<EditText>(R.id.etRegistrarEstudianteTelefono)

        val etRegistrarEstudianteEmail = findViewById<EditText>(R.id.etRegistrarEstudianteEmail)
        val etRegistrarEstudianteFechaNacimiento = findViewById<EditText>(R.id.etRegistrarEstudianteFechaNacimiento)
        val etRegistrarEstudianteCodigoCarrera = findViewById<EditText>(R.id.etRegistrarEstudianteCodigoCarrera)

        val btnSubmitEstudiante = findViewById<Button>(R.id.btnSubmitEstudiante)
        val imgBtnVolverRegistrarEstudiante= findViewById<ImageButton>(R.id.imgBtnVolverRegistrarEstudiante)

        val oracleDao = OracleDAO.instance

        imgBtnVolverRegistrarEstudiante.setOnClickListener {
            finish()
        }
        btnSubmitEstudiante.setOnClickListener {
            if (etRegistrarEstudianteId.text.isEmpty() || etRegistrarEstudianteNombre.text.isEmpty() || etRegistrarEstudiantePassword.text.isEmpty()
                || etRegistrarEstudianteTelefono.text.isEmpty() || etRegistrarEstudianteEmail.text.isEmpty() || etRegistrarEstudianteFechaNacimiento.text.isEmpty() ||
                etRegistrarEstudianteCodigoCarrera.text.isEmpty() ){
                Toast.makeText(this,"Hay algun espacio vacio", Toast.LENGTH_SHORT).show()
            }
            else{
                val id = etRegistrarEstudianteId.text.toString()
                val nombre = etRegistrarEstudianteNombre.text.toString()
                val password = etRegistrarEstudiantePassword.text.toString()
                val rol = "Estudiante"
                val telefono = etRegistrarEstudianteTelefono.text.toString()
                val email = etRegistrarEstudianteEmail.text.toString()
                val fechaNacimiento = etRegistrarEstudianteFechaNacimiento.text.toString()
                val codigoCarrera = etRegistrarEstudianteCodigoCarrera.text.toString()


                val usuarioEstudiante = AlumnoCreacion(id,nombre,password,rol,telefono,email,fechaNacimiento,codigoCarrera)
                oracleDao.postEstudiante(this,usuarioEstudiante)
                etRegistrarEstudianteId.text.clear()
                etRegistrarEstudianteNombre.text.clear()
                etRegistrarEstudiantePassword.text.clear()
                etRegistrarEstudianteTelefono.text.clear()
                etRegistrarEstudianteEmail.text.clear()
                etRegistrarEstudianteFechaNacimiento.text.clear()
                etRegistrarEstudianteCodigoCarrera.text.clear()
                Toast.makeText(this,"Estudiante insertado",Toast.LENGTH_SHORT).show()
            }
        }

    }
}