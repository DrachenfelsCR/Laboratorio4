package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etLoginUsername = findViewById<EditText>(R.id.etLoginUsername)
        val etLoginPassword = findViewById<EditText>(R.id.etLoginPassword)

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        val oracleDao = OracleDAO.instance
        var usuario = UsuarioDAO.instance

        btnSignIn.setOnClickListener {
            if (etLoginUsername.text.isEmpty() || etLoginPassword.text.isEmpty()){
                Toast.makeText(this,"Hay espacios vacios", Toast.LENGTH_SHORT).show()
            }
            else{
                oracleDao.Login(this,etLoginPassword.text.toString(),etLoginUsername.text.toString())
                var rol = usuario.getUsuario().rol
                if (rol == "Error")
                {
                    Toast.makeText(this,"Informacion incorrecta", Toast.LENGTH_SHORT).show()
                }
                else{
                    when(rol){
                        "Admin" -> {
                            etLoginUsername.text.clear()
                            etLoginPassword.text.clear()
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                        }
                        "Estudiante"->{
                            etLoginUsername.text.clear()
                            etLoginPassword.text.clear()
                            val i = Intent(this, AlumnoActivity::class.java)
                            i.putExtra("EXTRA_IDALUMNO",etLoginUsername.text.toString())
                            startActivity(i)
                        }
                        "Matriculador"->{
                            etLoginUsername.text.clear()
                            etLoginPassword.text.clear()
                            val i = Intent(this, MatriculadorActivity::class.java)
                            startActivity(i)
                        }
                        "Profesor"->{
                            etLoginUsername.text.clear()
                            etLoginPassword.text.clear()
                            val i = Intent(this, ProfesorActivity::class.java)
                            i.putExtra("EXTRA_IDPROFESOR",etLoginUsername.text.toString())
                            startActivity(i)
                        }
                    }


                }
            }
        }
    }
}