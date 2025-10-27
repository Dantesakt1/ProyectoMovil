package com.proyectomovil.APIs.AnimalesAPI

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.proyectomovil.BDLocal.AnimalApadrinado
import retrofit2.HttpException
import java.io.IOException

object AnimalesAPI {
    fun obtenerEspecies(
        owner: LifecycleOwner,
        context: Context,
        pagina: Int = 0,
        onSuccess: (List<AnimalApadrinado>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        AnimalesRepository.getEspecies(pagina).observe(owner) { result ->
            result.fold(
                onSuccess = { especies ->
                    if (especies.isEmpty()) {
                        onError(Exception("No se encontraron especies"))
                    } else {
                        onSuccess(especies)
                    }
                },
                onFailure = { e ->
                    val errorMessage = when {
                        e is HttpException && e.code() == 404 -> "URL no encontrada. Revise la configuración de la API"
                        e is IOException -> "Error de conexión. Verifique su conexión a internet"
                        else -> "Error: ${e.message}"
                    }
                    onError(Exception(errorMessage))
                }
            )
        }
    }
}