package com.proyectomovil

import android.os.Bundle
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
            val toast = Toast.makeText(
                this
                , "SIN CONEXION"
                , Toast.LENGTH_SHORT).show()
        }

        //se inicializan las variables
        val edUser: EditText = findViewById(R.id.ed_user)
        val edPassword: EditText = findViewById(R.id.ed_password)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val txMensajeLogin: TextView = findViewById(R.id.tx_msj_login)

        btnLogin.setOnClickListener {
            val username = edUser.text.toString()
            val password = edPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                txMensajeLogin.text = "Complete todos los campos"
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
                    // Aquí puedes navegar a otra actividad si quieres
                },
                onError = { error ->
                    println("Login incorrecto: ${error.message}")
                    txMensajeLogin.text = "Error en login"
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