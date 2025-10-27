package com.proyectomovil.BDLocal

data class AnimalApadrinado(
    val id: Int,
    val nombre: String,
    val especie: String,
    val imagen: String?,
    val tieneActualizacion: Boolean = false,
    val aporteMensual: Double = 0.0,
    val estadoConservacion: String? = null,
    val estaApadrinado: Boolean = false,
    val usuarioId: Int? = null  // ID del usuario que apadrina
)