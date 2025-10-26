package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.proyectomovil.conexionWAN.ValidarConexionWAN

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        try {
            setContentView(R.layout.activity_admin)

            //funcion heredada para verificar conexion a internet
            if(ValidarConexionWAN.isOnline(this)){
                println("conectado")
            }else{
                println("sin conexion")
                Toast.makeText(this, "SIN CONEXION", Toast.LENGTH_SHORT).show()
            }

            // Inicializar vistas
            val btnUsuarios: Button = findViewById(R.id.btn_gestionar_usuarios)
            val btnAnimales: Button = findViewById(R.id.btn_gestion_animales)
            val msjeBienvenida: TextView = findViewById(R.id.tx_bienvenido)

            // Obtener el username del intent
            val usuarioDesdeOtroActivity = intent.getStringExtra("username") ?: "Administrador"

            // Setear el mensaje de bienvenida
            msjeBienvenida.text = "¡Hola $usuarioDesdeOtroActivity!"

            btnUsuarios.setOnClickListener {
                val nuevaVentana = Intent(this, GestionUsuarios::class.java)
                startActivity(nuevaVentana)
            }

            btnAnimales.setOnClickListener {
                val nuevaVentana = Intent(this, GestionAnimales::class.java)
                startActivity(nuevaVentana)
            }

            // Aplicar window insets
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        } catch (e: Exception) {
            println("Error en AdminActivity: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Error al cargar la actividad: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}