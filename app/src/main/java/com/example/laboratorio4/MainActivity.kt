package com.example.laboratorio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        var navView = findViewById<NavigationView>(R.id.navView)
        val userDao = UsuarioDAO.instance

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.carrerasOption -> {
                val i = Intent(this, CarreraActivity::class.java)
                //i.putExtra("USER_EXTRA",user?.user!!)
                startActivity(i)
                }
                R.id.ciclosOption -> {
                    val i = Intent(this, CiclosActivity::class.java)
                    //i.putExtra("USER_EXTRA",user?.user!!)
                    startActivity(i)
                }
                R.id.usersOption -> {
                    val i = Intent(this, UserManagementActivity::class.java)
                    //i.putExtra("USER_EXTRA",user?.user!!)
                    startActivity(i)
                }
                R.id.HistorialAdminOption -> {
                    val i = Intent(this, HistorialAlumnoAdminActivity::class.java)
                    //i.putExtra("USER_EXTRA",user?.user!!)
                    startActivity(i)
                }
                R.id.adminLogoutOption -> {
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