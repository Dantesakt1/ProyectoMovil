package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_user)

        // Configurar la barra de navegación
        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)

        // Marcar "Perfil" como seleccionado
        barraNavegacion.selectedItemId = R.id.pantalla_perfil

        // Manejar clicks en la barra
        barraNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantalla_inicio -> {
                    // Volver a UserActivity (Inicio)
                    finish() // Cierra esta activity y vuelve
                    true
                }
                R.id.pantalla_apadrinamientos -> {
                    // Ir a Apadrinamientos
                    val intent = Intent(this, Apadrinamientos::class.java)
                    startActivity(intent)
                    finish() // Cierra esta para no acumular activities
                    true
                }
                R.id.pantalla_perfil -> {
                    // Ya estás en Perfil, no hacer nada
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}