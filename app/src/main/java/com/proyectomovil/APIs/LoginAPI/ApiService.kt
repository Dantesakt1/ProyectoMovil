package com.proyectomovil.APIs.LoginAPI

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

data class UsuarioInsertRequest(
    val username: String,
    val password: String
)

data class InsertResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val accessToken: String,
    val refreshToken: String,
    val success: Boolean = true,
    val message: String? = null
)

interface ApiService{
    @GET("users")
    suspend fun getUsuarios(): List<Usuario>

    @POST("auth/login")
    suspend fun insertUsuario(@Body request: UsuarioInsertRequest): InsertResponse
}