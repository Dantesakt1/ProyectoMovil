package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proyectomovil.APIs.AnimalesAPI.AnimalesAPI
import com.proyectomovil.APIs.LoginAPI.UsuariosRepository
import com.proyectomovil.BDLocal.AnimalApadrinado
import com.proyectomovil.BDLocal.AnimalesAdapter
import com.proyectomovil.BDLocal.ApadrinamientosManager
import kotlinx.coroutines.launch

class Apadrinamientos : AppCompatActivity() {

    //Para la bd local de los animales apadrinados
    private lateinit var recyclerAnimales: RecyclerView
    private lateinit var adapter: AnimalesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apadrinamientos)

        // Configurar RecyclerView
        recyclerAnimales = findViewById(R.id.recycler_animales)
        recyclerAnimales.layoutManager = LinearLayoutManager(this)

        // Obtener ID del usuario actual
        val usuarioId = getSharedPreferences("user_prefs", MODE_PRIVATE)
            .getInt("usuario_id", -1)

        // Si venimos desde sugerencias, procesar el animal a apadrinar
        if (intent.getBooleanExtra("desde_sugerencias", false)) {
            val animalId = intent.getIntExtra("animal_id", -1)
            val animalNombre = intent.getStringExtra("animal_nombre") ?: ""

            if (animalId != -1) {
                val nuevoAnimal = AnimalApadrinado(
                    id = animalId,
                    nombre = animalNombre,
                    especie = "",  // Se podría obtener de la API
                    imagen = null,
                    tieneActualizacion = false,
                    aporteMensual = 10.0,  // Valor por defecto
                    estadoConservacion = null,
                    estaApadrinado = true
                )

                // Guardar el apadrinamiento
                ApadrinamientosManager.apadrinarAnimal(this, usuarioId, nuevoAnimal)
                Toast.makeText(this, "¡Has apadrinado a $animalNombre!", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar solo los animales apadrinados por este usuario
        val animalesApadrinados = ApadrinamientosManager.obtenerApadrinamientosPorUsuario(this, usuarioId)
            .mapNotNull { animalId ->
                // Aquí podrías obtener los detalles completos del animal desde la API
                // Por ahora usamos los datos básicos
                AnimalApadrinado(
                    id = animalId,
                    nombre = "Animal $animalId",
                    especie = "Especie",
                    imagen = null,
                    tieneActualizacion = false,
                    aporteMensual = 10.0,
                    estadoConservacion = null,
                    estaApadrinado = true
                )
            }

        fun actualizarEstadisticas(usuarioId: Int) {
            val apadrinamientos = ApadrinamientosManager.obtenerApadrinamientosPorUsuario(this, usuarioId)

            // Actualizar badges
            findViewById<TextView>(R.id.badge_animales).text = apadrinamientos.size.toString()

            val aporteMensual = apadrinamientos.size * 10.0  // $10 por animal
            findViewById<TextView>(R.id.badge_mensual).text = "$${aporteMensual.toInt()}"

            val totalDonado = aporteMensual * 3  // Ejemplo: 3 meses de apadrinamiento
            findViewById<TextView>(R.id.badge_total).text = "$${totalDonado.toInt()}"

            findViewById<TextView>(R.id.badge_actualizaciones).text = "0"

            // Actualizar texto de logro
            findViewById<TextView>(R.id.texto_logro).text =
                "Has alcanzado $${totalDonado.toInt()} en donaciones totales"
        }

        fun mostrarOpcionesAnimal(animal: AnimalApadrinado) {
            // Mostrar diálogo con opciones:
            // - Ver detalles
            // - Cancelar apadrinamiento
            // - Ver historial de aportes
            Toast.makeText(this, "Opciones de ${animal.nombre}", Toast.LENGTH_SHORT).show()
        }

        adapter = AnimalesAdapter(animalesApadrinados) { animal ->
            // Mostrar opciones del animal apadrinado
            mostrarOpcionesAnimal(animal)
        }
        recyclerAnimales.adapter = adapter

        actualizarEstadisticas(usuarioId)

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