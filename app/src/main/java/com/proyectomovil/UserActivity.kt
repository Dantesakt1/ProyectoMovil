package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proyectomovil.APIs.AnimalesAPI.AnimalesAPI
import androidx.cardview.widget.CardView
import com.proyectomovil.BDLocal.AnimalApadrinado
import com.proyectomovil.BDLocal.ApadrinamientosManager

class UserActivity : AppCompatActivity() {

    private lateinit var contenedorSugerencias: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)

        contenedorSugerencias = findViewById(R.id.contenedor_sugerencias)
        progressBar = findViewById(R.id.progress_bar)

        
        fun mostrarSugerencias(sugerencias: List<AnimalApadrinado>) {
            progressBar.visibility = View.GONE
            contenedorSugerencias.removeAllViews()

            sugerencias.forEach { animal ->
                val cardView = LayoutInflater.from(this)
                    .inflate(R.layout.item_sugerencia, contenedorSugerencias, false) as CardView

                // Configurar la tarjeta
                cardView.findViewById<TextView>(R.id.nombre_especie).text = animal.nombre

                cardView.setOnClickListener {
                    val intent = Intent(this, Apadrinamientos::class.java).apply {
                        putExtra("animal_id", animal.id)
                        putExtra("animal_nombre", animal.nombre)
                        putExtra("desde_sugerencias", true)
                    }
                    startActivity(intent)
                }

                contenedorSugerencias.addView(cardView)
            }
        }

        fun cargarSugerencias() {
            progressBar.visibility = View.VISIBLE

            AnimalesAPI.obtenerEspecies(
                owner = this,
                context = this,
                pagina = 0,
                onSuccess = { animales ->
                    // Filtrar animales que no estén apadrinados por el usuario actual
                    val usuarioId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .getInt("usuario_id", -1)

                    val animalesDisponibles = animales.filter { animal ->
                        !ApadrinamientosManager.estaApadrinadoPorUsuario(
                            context = this,
                            usuarioId = usuarioId,
                            animalId = animal.id
                        )
                    }

                    // Tomar 3 sugerencias aleatorias
                    val sugerencias = animalesDisponibles.shuffled().take(3)

                    runOnUiThread {
                        mostrarSugerencias(sugerencias)
                    }
                },
                onError = { error ->
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Error cargando sugerencias: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        cargarSugerencias()

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