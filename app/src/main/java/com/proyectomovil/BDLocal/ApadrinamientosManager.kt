package com.proyectomovil.BDLocal

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ApadrinamientosManager {
    private const val PREFS_NAME = "apadrinamientos_prefs"
    private const val KEY_APADRINAMIENTOS = "apadrinamientos_por_usuario"

    // Estructura para guardar apadrinamientos por usuario
    private data class ApadrinamientoUsuario(
        val usuarioId: Int,
        val animalId: Int,
        val fechaApadrinamiento: Long = System.currentTimeMillis()
    )

    // Guardar apadrinamiento
    fun apadrinarAnimal(context: Context, usuarioId: Int, animal: AnimalApadrinado) {
        val apadrinamientos = obtenerApadrinamientos(context).toMutableList()

        // Crear nuevo apadrinamiento
        val nuevoApadrinamiento = ApadrinamientoUsuario(
            usuarioId = usuarioId,
            animalId = animal.id
        )

        apadrinamientos.add(nuevoApadrinamiento)
        guardarApadrinamientos(context, apadrinamientos)
    }

    // Obtener apadrinamientos de un usuario
    fun obtenerApadrinamientosPorUsuario(context: Context, usuarioId: Int): List<Int> {
        return obtenerApadrinamientos(context)
            .filter { it.usuarioId == usuarioId }
            .map { it.animalId }
    }

    // Verificar si un animal está apadrinado por un usuario
    fun estaApadrinadoPorUsuario(context: Context, usuarioId: Int, animalId: Int): Boolean {
        return obtenerApadrinamientos(context)
            .any { it.usuarioId == usuarioId && it.animalId == animalId }
    }

    private fun obtenerApadrinamientos(context: Context): List<ApadrinamientoUsuario> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_APADRINAMIENTOS, "[]")
        val type = object : TypeToken<List<ApadrinamientoUsuario>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun guardarApadrinamientos(context: Context, apadrinamientos: List<ApadrinamientoUsuario>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(apadrinamientos)
        prefs.edit().putString(KEY_APADRINAMIENTOS, json).apply()
    }
}