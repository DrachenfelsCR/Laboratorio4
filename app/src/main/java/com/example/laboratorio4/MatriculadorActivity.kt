package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MatriculadorActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriculador)

        var drawerLayoutMatriculador = findViewById<DrawerLayout>(R.id.drawerLayoutMatriculador)
        var navViewMatriculador = findViewById<NavigationView>(R.id.navViewMatriculador)


        toggle = ActionBarDrawerToggle(this, drawerLayoutMatriculador, R.string.open,R.string.close)
        drawerLayoutMatriculador.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navViewMatriculador.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.matricularOption -> {
                    val i = Intent(this, MatricularActivity::class.java)
                    //i.putExtra("USER_EXTRA",user?.user!!)
                    startActivity(i)
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