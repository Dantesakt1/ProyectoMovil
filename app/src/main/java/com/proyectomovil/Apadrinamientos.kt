package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Apadrinamientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apadrinamientos)

        // Configurar la barra de navegación
        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)

        // Marcar "Apadrinamientos" como seleccionado
        barraNavegacion.selectedItemId = R.id.pantalla_apadrinamientos

        // Manejar clicks en la barra
        barraNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantalla_inicio -> {
                    // Volver a UserActivity (Inicio)
                    finish()
                    true
                }
                R.id.pantalla_apadrinamientos -> {
                    // Ya estás en Apadrinamientos, no hacer nada
                    true
                }
                R.id.pantalla_perfil -> {
                    // Ir a Perfil
                    val intent = Intent(this, PerfilUser::class.java)
                    startActivity(intent)
                    finish()
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