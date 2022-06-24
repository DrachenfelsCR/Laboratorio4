package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UserManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        val btnAddNewStudent = findViewById<Button>(R.id.btnAddNewStudent)
        val btnAgregarProfesor = findViewById<Button>(R.id.btnAgregarProfesor)
        val btnAgregarNuevoUsuario = findViewById<Button>(R.id.btnAgregarNuevoUsuario)
        val btnVolverUserManagement =  findViewById<Button>(R.id.btnVolverUserManagement)

        btnAddNewStudent.setOnClickListener {
            val i = Intent(this, RegisterEstudianteActivity::class.java)
            startActivity(i)
        }
        btnAgregarProfesor.setOnClickListener {
            val i = Intent(this, RegistrarProfesorActivity::class.java)
            startActivity(i)
        }
        btnAgregarNuevoUsuario.setOnClickListener {
            val i = Intent(this, RegistrarUsuarioActivity::class.java)
            startActivity(i)
        }
        btnVolverUserManagement.setOnClickListener {
            finish()
        }
    }
}