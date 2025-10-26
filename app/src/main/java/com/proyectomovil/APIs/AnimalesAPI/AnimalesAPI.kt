package com.proyectomovil.APIs.AnimalesAPI

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

object AnimalesAPI {

    // Obtener lista de especies en peligro
    fun obtenerEspecies(
        owner: LifecycleOwner,
        context: Context,
        pagina: Int = 0,
        onSuccess: (List<Species>) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        owner.lifecycleScope.launch {
            val res = AnimalesRepository.fetchSpecies(pagina)
            res.onSuccess { response ->
                onSuccess(response.result)
            }.onFailure { e ->
                Toast.makeText(context, "Error cargando especies: ${e.message}", Toast.LENGTH_LONG).show()
                onError?.invoke(e)
            }
        }
    }

    // Buscar especie específica
    fun buscarEspecie(
        owner: LifecycleOwner,
        context: Context,
        nombre: String,
        onSuccess: (SpeciesDetail?) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        owner.lifecycleScope.launch {
            val res = AnimalesRepository.fetchSpeciesByName(nombre)
            res.onSuccess { response ->
                onSuccess(response.result.firstOrNull())
            }.onFailure { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                onError?.invoke(e)
            }
        }
    }

    // Obtener especies de Chile (o cualquier país)
    fun obtenerEspeciesPorPais(
        owner: LifecycleOwner,
        context: Context,
        codigoPais: String = "CL", // CL = Chile
        onSuccess: (List<Species>) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        owner.lifecycleScope.launch {
            val res = AnimalesRepository.fetchSpeciesByCountry(codigoPais)
            res.onSuccess { response ->
                onSuccess(response.result)
            }.onFailure { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                onError?.invoke(e)
            }
        }
    }
}