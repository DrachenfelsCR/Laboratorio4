package com.example.laboratorio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class RegistrarUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        val imgBtnVolverRegistrarUsuario = findViewById<ImageButton>(R.id.imgBtnVolverRegistrarUsuario)

        val etRegistrarUsuarioId = findViewById<EditText>(R.id.etRegistrarUsuarioId)
        val etRegistrarUsuarioPassword = findViewById<EditText>(R.id.etRegistrarUsuarioPassword)
        val etRegistrarUsuarioRol = findViewById<EditText>(R.id.etRegistrarUsuarioRol)

        val btnSubmitRegistrarUsuario = findViewById<Button>(R.id.btnSubmitRegistrarUsuario)

        val oracleDao = OracleDAO.instance

        imgBtnVolverRegistrarUsuario.setOnClickListener {
            finish()
        }

        btnSubmitRegistrarUsuario.setOnClickListener {
            if (etRegistrarUsuarioId.text.isEmpty() || etRegistrarUsuarioPassword.text.isEmpty() || etRegistrarUsuarioRol.text.isEmpty()){
                Toast.makeText(this,"Hay espacios vacios",Toast.LENGTH_SHORT).show()
            }
            else if(etRegistrarUsuarioRol.text.toString() != "Admin" && etRegistrarUsuarioRol.text.toString() != "Matriculador"){
                Toast.makeText(this,"Solo puedes agregar roles Admin o Matriculador",Toast.LENGTH_LONG).show()
            }
            else{
                val id = etRegistrarUsuarioId.text.toString()
                val password = etRegistrarUsuarioPassword.text.toString()
                val rol = etRegistrarUsuarioRol.text.toString()
                val userCreated = UsuarioCreacion(id,password,rol)
                oracleDao.postUsuario(this,userCreated)
                etRegistrarUsuarioId.text.clear()
                etRegistrarUsuarioPassword.text.clear()
                etRegistrarUsuarioRol.text.clear()
                Toast.makeText(this,"Usuario Insertado",Toast.LENGTH_SHORT).show()
            }
        }

    }
}