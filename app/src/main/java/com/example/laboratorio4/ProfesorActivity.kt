package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ProfesorActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesor)
        val idProfesor = intent.getStringExtra("EXTRA_IDPROFESOR")
        var drawerLayoutProfesor = findViewById<DrawerLayout>(R.id.drawerLayoutProfesor)
        var navViewProfesor = findViewById<NavigationView>(R.id.navViewProfesor)
        val userDao = UsuarioDAO.instance

        toggle = ActionBarDrawerToggle(this, drawerLayoutProfesor, R.string.open,R.string.close)
        drawerLayoutProfesor.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navViewProfesor.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profesorOption -> {
                    val i = Intent(this, ProfesorAlumnosActivity::class.java)
                    i.putExtra("EXTRA_IDPROFESOR",idProfesor)
                    startActivity(i)
                }
                R.id.logoutProfesor -> {
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