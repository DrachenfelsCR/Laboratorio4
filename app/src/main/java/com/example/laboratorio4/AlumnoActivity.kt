package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AlumnoActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        var drawerLayoutAlumno = findViewById<DrawerLayout>(R.id.drawerLayoutAlumno)
        var navViewAlumno = findViewById<NavigationView>(R.id.navViewAlumno)
        val userDao = UsuarioDAO.instance

        val userId = intent.getStringExtra("EXTRA_IDALUMNO")

        toggle = ActionBarDrawerToggle(this, drawerLayoutAlumno, R.string.open,R.string.close)
        drawerLayoutAlumno.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navViewAlumno.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.historialAcademicoOption -> {
                    val i = Intent(this, EstudianteHistorialActivity::class.java)
                    i.putExtra("EXTRA_IDALUMNO",userId)
                    startActivity(i)
                }
                R.id.logoutEstudianteOption -> {
                   userDao.setUsuario(Usuario("",""))
                    finish()
                }
            }
            true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}