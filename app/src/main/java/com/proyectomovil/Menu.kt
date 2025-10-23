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

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        //funcion heredada para verificar conexion a internet
        if(ValidarConexionWAN.isOnline(this)){
            println("conectado")
        }else{
            println("sin conexion")
            val toast = Toast.makeText(
                this
                , "SIN CONEXION"
                , Toast.LENGTH_SHORT).show()
        }

        val btnUsuarios: Button = findViewById(R.id.btn_gestionar_usuarios)
        val btnUAnimales: Button = findViewById(R.id.btn_gestion_animales)

        //ACTIVITY DESTINO
        val msjeBienvenida: TextView = findViewById(R.id.tx_bienvenido)
        //creo variable asigno valor recibido desde otro activity
        val usuarioDesdeOtroActivity = intent.getStringExtra("sesion")
        //seteo un TextView reemplazando el texto por el contenido.
        msjeBienvenida.text = ("¡Hola " + usuarioDesdeOtroActivity.toString() + "!")


        btnUsuarios.setOnClickListener {
            val nuevaVentana = Intent(this, GestionUsuarios::class.java)

            startActivity(nuevaVentana)
        }

        btnUAnimales.setOnClickListener {
            val nuevaVentana = Intent(this, GestionAnimales::class.java)

            startActivity(nuevaVentana)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}