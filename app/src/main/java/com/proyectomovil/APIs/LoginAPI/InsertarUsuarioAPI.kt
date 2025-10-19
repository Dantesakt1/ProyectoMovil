package com.proyectomovil.APIs.LoginAPI

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

object InsertarUsuarioAPI {

    fun insertarUsuario(
        owner: LifecycleOwner,
        context: Context,
        username: String,
        password: String,
        onSuccess: ((String?) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ){
        val request = UsuarioInsertRequest(
            username = username.trim(),
            password = password.trim()
        )

        owner.lifecycleScope.launch {
            val res = UsuariosRepository.insertUsuario(request)
            res.onSuccess { r ->
                Toast.makeText(context, r.message ?: "Insert OK", Toast.LENGTH_LONG).show()
                onSuccess?.invoke(r.message)
            }.onFailure { e ->
                Toast.makeText(context, "Error insertando: ${e.message}", Toast.LENGTH_LONG).show()
                onError?.invoke(e)
            }
        }
    }
}