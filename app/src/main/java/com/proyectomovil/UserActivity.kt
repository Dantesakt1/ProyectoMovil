package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)

        // Configurar la barra de navegación
        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)

        // Marcar "Inicio" como seleccionado por defecto
        barraNavegacion.selectedItemId = R.id.pantalla_inicio

        // Manejar los clicks en los botones
        barraNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantalla_inicio -> {
                    // Ya estás en inicio, no hacer nada
                    true
                }
                R.id.pantalla_apadrinamientos -> {
                    // Abrir Activity de Apadrinamientos
                    val intent = Intent(this, Apadrinamientos::class.java)
                    startActivity(intent)
                    // No terminar esta activity para poder volver
                    true
                }
                R.id.pantalla_perfil -> {
                    // Abrir Activity de Perfil
                    val intent = Intent(this, PerfilUser::class.java)
                    startActivity(intent)
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