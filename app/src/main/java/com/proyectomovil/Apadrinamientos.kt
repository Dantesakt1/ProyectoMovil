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

        // Cargar animales GUARDADOS de SharedPreferences
        val animalesGuardados = ApadrinamientosManager.obtenerAnimales(this)

        adapter = AnimalesAdapter(animalesGuardados) { animal ->
            Toast.makeText(this, "Ver detalles de ${animal.nombre}", Toast.LENGTH_SHORT).show()
        }
        recyclerAnimales.adapter = adapter

        // ACTUALIZAR TARJETAS CON ESTADÍSTICAS
        // ═══════════════════════════════════════════════════════════
        fun actualizarEstadisticas() {
            val stats = ApadrinamientosManager.obtenerEstadisticas(this)

            findViewById<TextView>(R.id.badge_animales).text = stats.totalAnimales.toString()
            findViewById<TextView>(R.id.badge_total).text = "$${stats.totalDonado.toInt()}"
            findViewById<TextView>(R.id.badge_mensual).text = "$${stats.aporteMensual.toInt()}"
            findViewById<TextView>(R.id.badge_actualizaciones).text = stats.actualizaciones.toString()

            findViewById<TextView>(R.id.texto_logro).text =
                "Has alcanzado $${stats.totalDonado.toInt()} en donaciones totales"
        }

        // Actualizar estadísticas en las tarjetas
        actualizarEstadisticas()

        fun cargarEspeciesDisponibles(pagina: Int = 0) {
            // Obtener especies desde la API de Animales
            AnimalesAPI.obtenerEspecies(
                owner = this,
                context = this,
                pagina = pagina,
                onSuccess = { speciesList ->
                    // Obtener usuarios desde la API de usuarios
                    lifecycleScope.launch {
                        val usuariosResult = UsuariosRepository.fetchUsuarios()
                        usuariosResult.onSuccess { usuarios ->
                            // Convertir especies a AnimalApadrinado y asignar owner
                            val animales = speciesList.mapIndexed { index, especie ->
                                AnimalApadrinado(
                                    id = especie.taxonId,
                                    nombre = especie.commonName ?: especie.scientificName,
                                    especie = especie.scientificName,
                                    imagen = null,
                                    tieneActualizacion = false,
                                    aporteMensual = 0.0,
                                    owner = if (usuarios.isNotEmpty())
                                        usuarios[index % usuarios.size].username
                                    else null
                                )
                            }

                            // Guardar en BD local y actualizar UI
                            ApadrinamientosManager.guardarAnimales(this@Apadrinamientos, animales)
                            runOnUiThread {
                                adapter.actualizarAnimales(animales)
                                actualizarEstadisticas()
                            }
                        }.onFailure { e ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@Apadrinamientos,
                                    "Error cargando usuarios: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                onError = { e ->
                    Toast.makeText(
                        this,
                        "Error cargando especies: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        // Cargar especies disponibles de la API
        cargarEspeciesDisponibles()

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