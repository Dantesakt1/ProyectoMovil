package com.proyectomovil.APIs.LoginAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UsuariosRepository {

    suspend fun fetchUsuarios(): Result<List<Usuario>> = withContext(Dispatchers.IO){
        try {
            Result.success(ApiUsuariosClient.service.getUsuarios())
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun insertUsuario(request: UsuarioInsertRequest): Result<InsertResponse> = withContext(
        Dispatchers.IO){
        try {
            Result.success(ApiUsuariosClient.service.insertUsuario(request))
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}