package com.proyectomovil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.proyectomovil.APIs.LoginAPI.InsertarUsuarioAPI
import com.proyectomovil.conexionWAN.ValidarConexionWAN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //funcion heredada para verificar conexion a internet
        if(ValidarConexionWAN.isOnline(this)){
            println("conectado")
        }else{
            println("sin conexion")
            Toast.makeText(
                this
                , "SIN CONEXION"
                , Toast.LENGTH_SHORT).show()
        }

        //se inicializan las variables
        val edUser: EditText = findViewById(R.id.ed_user)
        val edPassword: EditText = findViewById(R.id.ed_password)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val txMensajeLogin: TextView = findViewById(R.id.tx_msj_login)
        val cardError: View = findViewById(R.id.card_error)

        // Lista de usuarios administradores (usuario y contraseña)
        val adminUsers = mapOf(
            "emilys" to "emilypass",
            "liamg" to "liamgpass",
            "noahh" to "noahhpass"
        )

        // Función para verificar si un usuario es administrador
        fun isAdmin(username: String, password: String): Boolean {
            return adminUsers[username] == password
        }

        btnLogin.setOnClickListener {
            val username = edUser.text.toString()
            val password = edPassword.text.toString()

            // Ocultar mensaje de error anterior
            txMensajeLogin.visibility = View.GONE
            cardError.visibility = View.GONE

            if (username.isEmpty() || password.isEmpty()) {
                txMensajeLogin.text = "Complete todos los campos"
                txMensajeLogin.visibility = View.VISIBLE
                cardError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Verificar si es un usuario administrador
            if (isAdmin(username, password)) {
                println("Login de administrador correcto")
                Toast.makeText(this, "Bienvenido Administrador!", Toast.LENGTH_SHORT).show()

                // Navegar a la actividad de admin
                val intent = Intent(this, AdminActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                finish() // Cierra el MainActivity para que no pueda volver con el botón atrás
                return@setOnClickListener
            }

            InsertarUsuarioAPI.insertarUsuario(
                owner = this,
                context = this,
                username = username,
                password = password,
                onSuccess = { message ->
                    println("Login correcto")
                    txMensajeLogin.text = "Login exitoso"
                    Toast.makeText(this, "Bienvenido $username", Toast.LENGTH_SHORT).show()

                    // Navegar a la actividad de usuario normal
                    val intent = Intent(this, UserActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    finish()
                },
                onError = { error ->
                    println("Login incorrecto: ${error.message}")
                    txMensajeLogin.text = "Error en login"
                    txMensajeLogin.visibility = View.VISIBLE
                    cardError.visibility = View.VISIBLE
                    Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}